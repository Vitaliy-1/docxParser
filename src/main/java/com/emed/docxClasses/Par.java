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

public class Par implements DocxElement {

    private List<ElementProperties> parProperties;
    private List<Text> content;

    public Par(Node node) throws XPathExpressionException {

        /* Initializing paragraph properties */
        List<ElementProperties> elementProperties = new ArrayList<>();
        XPathExpression expr = DocxDocument.documentXpath.compile("w:pPr/child::node()");
        NodeList nodeList = (NodeList) expr.evaluate(node, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i) != null) {
                Property property = new Property(nodeList.item(i));
                elementProperties.add(property);
            }
        }
        this.parProperties = elementProperties;

        /* Initializing Text nodes inside paragraphs */
        List<Text> content = new ArrayList<>();
        XPathExpression exprText = DocxDocument.documentXpath.compile("w:r");
        NodeList nodeListText = (NodeList) exprText.evaluate(node, XPathConstants.NODESET);
        for (int y = 0; y < nodeListText.getLength(); y++) {
            Node textNode = nodeListText.item(y);
            Text text= new Text(textNode);
            content.add(text);
        }
        this.content = content;
    }

    @Override
    public List<ElementProperties> getProperties() throws XPathExpressionException {
        return parProperties;
    }

    @Override
    public List<Text> getContent() {
        return content;
    }
}
