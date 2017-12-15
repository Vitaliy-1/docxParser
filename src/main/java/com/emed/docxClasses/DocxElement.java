package com.emed.docxClasses;

import javax.xml.xpath.XPathExpressionException;
import java.util.List;

public interface DocxElement {

    public List getContent();

    public List<ElementProperties> getProperties() throws XPathExpressionException;
}
