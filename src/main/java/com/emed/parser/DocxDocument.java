package com.emed.parser;

import com.emed.interfaces.DocxElement;
import com.emed.docxClasses.Par;
import com.emed.docxClasses.Table;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DocxDocument {

    public static XPath documentXpath;
    private Document domDocument;
    private List<DocxElement> docxElements;

    public DocxDocument(FileSystem zipFS) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        Path documentXmlPath = zipFS.getPath("/word/document.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        this.domDocument = documentBuilder.parse(Files.newInputStream(documentXmlPath));
        documentXpath = XPathFactory.newInstance().newXPath();

        /* Get Namespace from XML Document for XPath */
        NamedNodeMap namedNodeMap = domDocument.getDocumentElement().getAttributes();
        HashMap<String, String> namespaceMap = new HashMap<String, String>();
        if (namedNodeMap != null) {
            for (int i = 0; i < namedNodeMap.getLength(); i++) {
                namespaceMap.put(namedNodeMap.item(i).getLocalName(), namedNodeMap.item(i).getTextContent());
            }
        }

        SimpleNamespaceContext namespaces = new SimpleNamespaceContext(namespaceMap);
        documentXpath.setNamespaceContext(namespaces);

        /* Initialize content of DOCX document.xml */
        XPathExpression expr = documentXpath.compile("//w:body/child::node()");
        NodeList nodeList = (NodeList) expr.evaluate(domDocument, XPathConstants.NODESET);
        List<DocxElement> docxElements = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().contains("w:tbl")) {
                Table table = new Table(node);
                docxElements.add(table);
            } else if (node.getNodeName().contains("w:p")) {
                Par par = new Par(node);
                docxElements.add(par);
            }
        }
        this.docxElements = docxElements;
    }

    private boolean isTableTitle(Node node) throws XPathExpressionException {
        XPathExpression expr = documentXpath.compile("w:r/w:t");
        NodeList nodeList = (NodeList) expr.evaluate(node, XPathConstants.NODESET);

        return true;
    }

    public List<DocxElement> getContent() throws XPathExpressionException {
        return docxElements;
    }
    public Document getDocument() {
        return domDocument;
    }
}
