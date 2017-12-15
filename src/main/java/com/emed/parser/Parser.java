package com.emed.parser;

import com.emed.docxClasses.DocxElement;
import com.emed.docxClasses.ElementProperties;
import com.emed.docxClasses.Par;
import com.emed.docxClasses.Text;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Parser {
    @SuppressWarnings("unchecked")
    public static void main (String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {

        DocxArchive docxArchive = new DocxArchive(new File("example.docx"));
        DocxDocument docxDocument = docxArchive.getDocxDocument();
        List<DocxElement> docxElements = docxDocument.getContent();
        for (DocxElement docxElement : docxElements) {
            /*
            if (docxElement.getClass().getName().equals("com.emed.docxClasses.Par")) {
                List<ElementProperties> parPrs = docxElement.getProperties();
                for (ElementProperties parPr : parPrs) {
                    System.out.println("properties of a new paragraph: " + parPr.getPropertyName());
                }
                List<Text> texts =  docxElement.getContent();
                for (Text text : texts) {
                    for (ElementProperties elementProperties : text.getProperties()) {
                        System.out.println("prop: " + elementProperties.getPropertyName());
                        Map map = elementProperties.getPropertyMap();
                        map.forEach((k,v) -> System.out.println("name: "+k+" --- value: " +v));
                    }
                    System.out.println("text: " + text.getText());
                }
            }
            */
        }
    }

}
