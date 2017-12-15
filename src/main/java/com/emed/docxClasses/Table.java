package com.emed.docxClasses;

import com.emed.parser.DocxDocument;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

public class Table implements DocxElement {

    private List<ElementProperties> tblProperties;
    private List<DocxElement> rows;

    public Table (Node node) throws XPathExpressionException {

        /* Initializing Table properties */
        List<ElementProperties> elementProperties = new ArrayList<>();
        XPathExpression expr = DocxDocument.documentXpath.compile("w:tblPr/child::node()");
        NodeList nodeList = (NodeList) expr.evaluate(node, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i) != null) {
                Property property = new Property(nodeList.item(i));
                elementProperties.add(property);
            }
        }
        this.tblProperties = elementProperties;

        /* Initializing Table Rows */
        List<DocxElement> rowElements = new ArrayList<>();
        XPathExpression expr2 = DocxDocument.documentXpath.compile("w:tr");
        NodeList rowNodeList = (NodeList) expr2.evaluate(node, XPathConstants.NODESET);
        for (int y = 0; y < rowNodeList.getLength(); y++) {
            if (rowNodeList.item(y) != null) {
                Row row = new Row(rowNodeList.item(y));
                rowElements.add(row);
            }
        }
        this.rows = rowElements;
    }

    @Override
    public List<ElementProperties> getProperties() throws XPathExpressionException {
        return tblProperties;
    }

    @Override
    public List<DocxElement> getContent() {
        return rows;
    }
}
