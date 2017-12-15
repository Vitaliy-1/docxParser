package com.emed.docxClasses;

import com.emed.parser.DocxDocument;
import org.w3c.dom.NamedNodeMap;
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

    public Cell(Node node, int cellNumber) throws XPathExpressionException {
        /* Retrieving the number of cell in a row */
        this.cellNumber = cellNumber + 1;

        /* Initializing paragraphs inside table cells */
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

        /* Retrieving colspan number */
        XPathExpression colspanExpr = DocxDocument.documentXpath.compile("w:tcPr/w:gridSpan/@w:val");
        String gridspanNodeValue = (String) colspanExpr.evaluate(node, XPathConstants.STRING);
        if (!gridspanNodeValue.isEmpty()) {
            this.colspan = Integer.parseInt(gridspanNodeValue);
        }

        /* Retrieving rowspan number */
        XPathExpression rowspanStartExpr = DocxDocument.documentXpath.compile("w:tcPr/w:vMerge[@w:val='restart']");
        Node rowMergedNode = (Node) rowspanStartExpr.evaluate(node, XPathConstants.NODE);
        if (rowMergedNode != null) {
            // calculate node number
            XPathExpression cellListInNextRowExpr = DocxDocument.documentXpath.compile("parent::w:tr/following-sibling::w:tr[1]/w:tc");
            NodeList cellNodeListInNextRow = (NodeList) cellListInNextRowExpr.evaluate(node, XPathConstants.NODESET);
            int numberOfCells = 0;
            for (int y = 0; y < cellNodeListInNextRow.getLength(); y++) {
                XPathExpression cellNodeColspan = DocxDocument.documentXpath.compile("w:tcPr/w:gridSpan/@w:val");
                String colspanValue = (String) cellNodeColspan.evaluate(cellNodeListInNextRow.item(y), XPathConstants.STRING);
                if (colspanValue.isEmpty()) {
                    numberOfCells = numberOfCells + 1;
                } else {
                    numberOfCells = numberOfCells + Integer.parseInt(colspanValue);
                }
            }
            System.out.println(numberOfCells);

            XPathExpression rowspanStartParentExpr = DocxDocument.documentXpath.compile("parent::w:tr/following-sibling::w:tr/w:tc[" + 1 + "]");
            Node mergedCell = (Node) rowspanStartParentExpr.evaluate(node, XPathConstants.NODE);
        }

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

}
