package com.emed.docxClasses;

import com.emed.parser.DocxDocument;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

public class Text {
    private String text;
    private List<ElementProperties> textProperties;

    public Text(Node node) throws XPathExpressionException {

        /* writing a text */
        XPathExpression expr = DocxDocument.documentXpath.compile("w:t/text()");
        this.text = (String) expr.evaluate(node, XPathConstants.STRING);

        /* writing text properties */
        List<ElementProperties> elementProperties = new ArrayList<>();
        XPathExpression exprProp = DocxDocument.documentXpath.compile("w:rPr/child::node()");
        NodeList nodeList = (NodeList) exprProp.evaluate(node, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node nodeProp = nodeList.item(i);
            Property property = new Property(nodeProp);
            elementProperties.add(property);
        }
        this.textProperties = elementProperties;
    }

    public String getText() {
        return text;
    }

    public List<ElementProperties> getProperties() throws XPathExpressionException {
        return textProperties;
    }
}
