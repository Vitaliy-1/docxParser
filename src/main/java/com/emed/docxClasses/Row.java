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

public class Row implements DocxElement {

    private List<ElementProperties> rowProperties;
    private List<DocxElement> cells;

    public Row(Node node) throws XPathExpressionException {

        /* Initializing table cells */
        extractCells(node);

        /* Initializing row properties */
        extractProperties(node);
    }

    private void extractProperties(Node node) throws XPathExpressionException {
        List<ElementProperties> rowProperties = new ArrayList<>();
        XPathExpression expr2 = DocxDocument.documentXpath.compile("w:trPr");
        NodeList propList = (NodeList) expr2.evaluate(node, XPathConstants.NODESET);
        for (int y = 0; y < propList.getLength(); y++) {
            if (propList.item(y) != null) {
                Property property = new Property(propList.item(y));
                rowProperties.add(property);
            }
        }
        this.rowProperties = rowProperties;
    }

    private void extractCells(Node node) throws XPathExpressionException {
        List<DocxElement> cellList = new ArrayList<>();
        XPathExpression expr = DocxDocument.documentXpath.compile("w:tc");
        NodeList nodeList = (NodeList) expr.evaluate(node, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i) != null) {

                // calculating cell number for the constructor
                XPathExpression prevCellExpr = DocxDocument.documentXpath.compile("preceding-sibling::w:tc");
                NodeList precedSiblingNodes = (NodeList) prevCellExpr.evaluate(nodeList.item(i), XPathConstants.NODESET);
                int cellNumber = 1;
                for (int y = 0; y < precedSiblingNodes.getLength(); y++) {
                    Node precedSiblingNode = precedSiblingNodes.item(y);
                    XPathExpression isGridSpanExpr = DocxDocument.documentXpath.compile("w:tcPr/w:gridSpan/@w:val");
                    String colspan = (String) isGridSpanExpr.evaluate(precedSiblingNode, XPathConstants.STRING);
                    if (colspan.isEmpty()) {
                        cellNumber = cellNumber + 1;
                    } else {
                        cellNumber = cellNumber + Integer.parseInt(colspan);
                    }
                }
                Cell cell = new Cell(nodeList.item(i), cellNumber);
                cellList.add(cell);
            }
        }
        this.cells = cellList;
    }

    @Override
    public List<ElementProperties> getProperties() throws XPathExpressionException {
        return rowProperties;
    }

    @Override
    public List<DocxElement> getContent() {
        return cells;
    }
}
