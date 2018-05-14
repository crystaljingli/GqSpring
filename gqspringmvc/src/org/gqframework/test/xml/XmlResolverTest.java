package org.gqframework.test.xml;

import org.gqframework.beans.factory.xml.GqDelegatingEntityResolver;
import org.gqframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.gqframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * 测试XML验证
 */
public class XmlResolverTest {
    private static final String SCHEMA_LANGUAGE_ATTRIBUTE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String XSD_SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";
    public static void main(String args[]){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setAttribute(SCHEMA_LANGUAGE_ATTRIBUTE, XSD_SCHEMA_LANGUAGE);
        factory.setNamespaceAware(true);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new GqDelegatingEntityResolver());
            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) throws SAXException {

                }
                @Override
                public void error(SAXParseException exception) throws SAXException {
                    throw  exception;
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    throw  exception;
                }
            });
            InputSource is = new InputSource(new ClassPathResource("springtest.xml").getInputStream());
            Document document = builder.parse(is);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
