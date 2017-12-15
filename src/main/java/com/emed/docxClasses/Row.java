package com.emed.docxClasses;

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
        List<DocxElement> cellList = new ArrayList<>();
        XPathExpression expr = DocxDocument.documentXpath.compile("w:tc");
        NodeList nodeList = (NodeList) expr.evaluate(node, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i) != null) {
                Cell cell = new Cell(nodeList.item(i), i);
            }
        }
        this.cells = cellList;

        /* Initializing row properties */
        List<ElementProperties> rowProperties = new ArrayList<>();
        XPathExpression expr2 = DocxDocument.documentXpath.compile("w:trPr");
        NodeList propList = (NodeList) expr2.evaluate(node, XPathConstants.NODESET);
        for (int y = 0; y < propList.getLength(); y++) {
            if (propList.item(y) != null) {
                Property property = new Property(propList.item(y));
            }
        }
        this.rowProperties = rowProperties;

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
