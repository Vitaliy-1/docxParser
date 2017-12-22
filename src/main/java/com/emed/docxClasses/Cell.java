package com.emed.docxClasses;

import com.emed.interfaces.DocxElement;
import com.emed.interfaces.ElementProperties;
import com.emed.parser.DocxDocument;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

public class Cell implements DocxElement {

    private List<DocxElement> paragraphs;
    private List<ElementProperties> cellProperties;
    private int colspan;
    private int rowspan;
    private int cellNumber;
    private boolean isMerged;

    public Cell(Node node, int cellNumber) throws XPathExpressionException {
        /* Retrieving the number of cell in a row */
        this.cellNumber = cellNumber;

        /* check if cell is merged (for omitting in JATS) */
        XPathExpression mergeExpr = DocxDocument.documentXpath.compile("w:tcPr/w:vMerge[not(@*)]");
        Node mergedNode = (Node) mergeExpr.evaluate(node, XPathConstants.NODE);
        if (mergedNode != null) {
            isMerged = true;
        } else {
            isMerged = false;
        }

        /* Initializing paragraphs inside table cells */
        extractParagraphs(node);

        /* Retrieving colspan number */
        extractColspanNumber(node);

        /* Retrieving rowspan number */
        extractRowspanNumber(node);

    }

    private void extractRowspanNumber(Node node) throws XPathExpressionException {

        XPathExpression rowspanStartExpr = DocxDocument.documentXpath.compile("w:tcPr/w:vMerge[@w:val='restart']");
        Node rowMergedNode = (Node) rowspanStartExpr.evaluate(node, XPathConstants.NODE);
        rowspan = 1;

        if (rowMergedNode != null) {
            extractRowspanRecursion(node);
        }
    }

    private void extractRowspanRecursion(Node node) throws XPathExpressionException {
        // calculate the cell number in the new row
        XPathExpression cellListInNextRowExpr = DocxDocument.documentXpath.compile("parent::w:tr/following-sibling::w:tr[1]/w:tc");
        NodeList cellNodeListInNextRow = (NodeList) cellListInNextRowExpr.evaluate(node, XPathConstants.NODESET);

        int numberOfCells = 0; // counting number of cells in a row
        Node mergedNode = null; // retrieving possibly merged cell node
        for (int y = 0; y < cellNodeListInNextRow.getLength(); y++) {
            XPathExpression cellNodeColspan = DocxDocument.documentXpath.compile("w:tcPr/w:gridSpan/@w:val");
            String colspanValue = (String) cellNodeColspan.evaluate(cellNodeListInNextRow.item(y), XPathConstants.STRING);
            if (colspanValue.isEmpty()) {
                numberOfCells = numberOfCells + 1;
            } else {
                numberOfCells = numberOfCells + Integer.parseInt(colspanValue);
            }
            if (numberOfCells == cellNumber) {
               mergedNode = cellNodeListInNextRow.item(y);
            }
        }

        // check if the node is actually merged
        if (mergedNode != null) {
            XPathExpression actuallyMergedExpr = DocxDocument.documentXpath.compile("w:tcPr/w:vMerge");
            Node isActuallyMerged = (Node) actuallyMergedExpr.evaluate(mergedNode,  XPathConstants.NODE);
            if (isActuallyMerged != null) {
                rowspan++;
                extractRowspanRecursion(mergedNode);
            }
        }
    }

    private void extractColspanNumber(Node node) throws XPathExpressionException {
        XPathExpression colspanExpr = DocxDocument.documentXpath.compile("w:tcPr/w:gridSpan/@w:val");
        String gridspanNodeValue = (String) colspanExpr.evaluate(node, XPathConstants.STRING);
        if (!gridspanNodeValue.isEmpty()) {
            this.colspan = Integer.parseInt(gridspanNodeValue);
        }
    }

    private void extractParagraphs(Node node) throws XPathExpressionException {
        List<DocxElement> paragraphs = new ArrayList<>();
        XPathExpression expr = DocxDocument.documentXpath.compile("w:p");
        NodeList parNodeList = (NodeList) expr.evaluate(node, XPathConstants.NODESET);
        for (int i = 0; i < parNodeList.getLength(); i++) {
            if (parNodeList.item(i) != null) {
                Par par = new Par(parNodeList.item(i));
                paragraphs.add(par);
            }
        }
        this.paragraphs = paragraphs;
    }

    @Override
    public List<DocxElement> getContent() {
        return paragraphs;
    }

    @Override
    public List<ElementProperties> getProperties() {
        return cellProperties;
    }

    public int getColspan() {
        return colspan;
    }

    public  int getRowspan() {
        return rowspan;
    }

    public int getCellNumber() {
        return cellNumber;
    }

    public boolean isMerged() {
        return isMerged;
    }
}
