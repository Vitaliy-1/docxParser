package com.emed.docxClasses;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

public class Property implements ElementProperties {

    private String name;
    private Map<String, String> propertyMap;

    public Property(Node node) {
        Map<String, String> propertyMap = new HashMap<>();
        name = node.getNodeName();
        NamedNodeMap namedNodeMap = node.getAttributes();
        for (int i = 0; i < namedNodeMap.getLength(); i++) {
            propertyMap.put(namedNodeMap.item(i).getNodeName(), namedNodeMap.item(i).getTextContent());
        }
        this.propertyMap = propertyMap;
    }

    public String getPropertyName() {
        return name;
    }

    public Map<String, String> getPropertyMap() {
        return propertyMap;
    }

}

