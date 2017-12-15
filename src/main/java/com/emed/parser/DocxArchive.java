package com.emed.parser;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

public class DocxArchive {
    private DocxDocument docxDocument;

    public DocxArchive(File docxFile) throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        URI docxUri = URI.create("jar:" + docxFile.toURI());
        Map<String, String> zipProperties = new HashMap<>();
        zipProperties.put("encoding", "UTF-8");
        FileSystem zipFS = FileSystems.newFileSystem(docxUri, zipProperties);
        this.docxDocument = new DocxDocument(zipFS);
    }

    public DocxDocument getDocxDocument() {
        return docxDocument;
    }
}
