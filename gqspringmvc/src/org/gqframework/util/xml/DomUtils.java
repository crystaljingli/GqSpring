package org.gqframework.util.xml;

import org.gqframework.util.Assert;
import org.w3c.dom.*;

import java.util.List;

public abstract class DomUtils {

    public static String getChildElementValueByTagName(Element ele,String childEleName){
        Element childeEle = getChildElementByTagName(ele,childEleName);
        return childeEle != null ? getTextValue(childeEle) : null;
    }

    public static Element getChildElementByTagName(Element ele, String childEleName) {
        NodeList nodes = ele.getChildNodes();
        for(int i=0;i<nodes.getLength();i++){
            Node node = nodes.item(i);
            if(node instanceof Element && nodeNameMatch(node,childEleName)){
                return (Element) node;
            }
        }
        return null;
    }

    public static String getTextValue(Element valueEle) {
        Assert.notNull(valueEle, "Element must not be null");
        StringBuilder sb = new StringBuilder();
        NodeList nl = valueEle.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node item = nl.item(i);
            if ((item instanceof CharacterData && !(item instanceof Comment)) || item instanceof EntityReference) {
                sb.append(item.getNodeValue());
            }
        }
        return sb.toString();
    }

    private static boolean nodeNameMatch(Node node, String desiredName) {
        return (desiredName.equals(node.getNodeName()) || desiredName.equals(node.getLocalName()));
    }
}
