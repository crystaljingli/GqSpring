package org.gqframework.beans.factory.xml;

import org.gqframework.core.io.ClassPathResource;
import org.gqframework.core.io.DefaultResourceLoader;
import org.gqframework.core.io.Resource;
import org.gqframework.core.io.ResourceLoader;
import org.gqframework.lang.Nullable;
import org.gqframework.util.ClassUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class GqDelegatingEntityResolver implements EntityResolver{

    private final static  String DTD_SUFFIX = ".dtd";
    private  final  static  String XSD_SUFFIX  = ".xsd";
    private final XSDEntityResolver xsdEntityResolver;
    private final DTDEntityResolver dtdEntityResolver;
    private final ResourceLoader resourceLoader;

    public GqDelegatingEntityResolver(){
        this.xsdEntityResolver = new XSDEntityResolver(this.getClass().getClassLoader());
        this.dtdEntityResolver = new DTDEntityResolver();
        this.resourceLoader = new DefaultResourceLoader();
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        System.out.println(publicId + "::::::::::" + systemId);
        if (systemId != null) {
            if (systemId.endsWith(DTD_SUFFIX)) {
                return this.dtdEntityResolver.resolveEntity(publicId, systemId);
            }
            else if (systemId.endsWith(XSD_SUFFIX)) {
                return this.xsdEntityResolver.resolveEntity(publicId, systemId);
            }
            String resourcePath = "";

            String decodedSystemId = URLDecoder.decode(systemId,"utf-8");
            String givenUrl = new URL(decodedSystemId).toString();
            String systemRootUrl =  new File("").toURI().toURL().toString();
            if(givenUrl.startsWith(systemRootUrl)){
                resourcePath = givenUrl.substring(systemRootUrl.length());
            }

            if(resourcePath != null){
                Resource resource = this.resourceLoader.getResource(resourcePath);
                InputSource source = new InputSource(resource.getInputStream());
                source.setPublicId(publicId);
                source.setSystemId(systemId);
                return source;
            }
        }
        return null;
    }

    private class XSDEntityResolver implements EntityResolver{
        private final static String XSD_EXAMPLE_STRING = "http://www.springframework.org/schema/mvc/spring-mvc-3.2.xsd";
        public static final String DEFAULT_SCHEMA_MAPPINGS_LOCATION = "META-INF/spring.schemas";

        private volatile Map<String, String> schemaMappings;
        private final String schemaMappingsLocation;
        @Nullable
        private final ClassLoader classLoader;

        public XSDEntityResolver(@Nullable ClassLoader classLoader, String schemaMappingsLocation){
            this.classLoader = classLoader;
            this.schemaMappingsLocation = schemaMappingsLocation;
        }
        public XSDEntityResolver(@Nullable ClassLoader classLoader){
            this.classLoader = classLoader;
            this.schemaMappingsLocation = DEFAULT_SCHEMA_MAPPINGS_LOCATION;
        }

        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            System.out.println(systemId);
            if(systemId != null){
                String schemaLocation = getSchemaMappings().get(systemId);
                Resource resource = new ClassPathResource(schemaLocation,this.classLoader);
                InputSource is = new InputSource(resource.getInputStream());
                is.setPublicId(publicId);
                is.setSystemId(systemId);
                return is;
            }
            return null;
        }

        /**
         * 获取schema集合
         * @return
         */
        public Map<String, String> getSchemaMappings(){
            Map<String, String> schemaMappings = this.schemaMappings;
            if(schemaMappings == null){
                synchronized (this){
                    ClassLoader classLoaderToUse = this.classLoader;
                    if(classLoaderToUse == null){
                        classLoaderToUse =  ClassUtils.getDefaultClassLoader();
                    }
                    schemaMappings = new HashMap<>();
                    Enumeration<URL> schameEnum = null;
                    try {
                        schameEnum = classLoaderToUse.getResources(this.schemaMappingsLocation);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Properties prop = new Properties();
                    while(schameEnum.hasMoreElements()){
                        URL url = schameEnum.nextElement();
                        URLConnection conn = null;
                        InputStream is = null;
                        try {
                            conn = url.openConnection();
                            is = conn.getInputStream();
                            prop.load(is);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Enumeration<Object> keys = prop.keys();
                    while(keys.hasMoreElements()){
                        String key = (String)keys.nextElement();
                        schemaMappings.put(key,prop.getProperty(key));
                    }
                    this.schemaMappings = schemaMappings;
                }
            }
            return schemaMappings;
        }
    }

    private class DTDEntityResolver implements EntityResolver{
        //SPRING 配置示例
        private final static String DTD_EXAMPLE_STRING = "<!DOCTYPE beans PUBLIC  \"-//SPRING//DTD BEAN//EN\"  \"http://www.springframework.org/dtd/spring-beans.dtd\">";
        private final static String DTD_EXTENSION = ".dtd";
        private static final String DTD_NAME = "spring-beans";

        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            InputSource is = null;
            if(systemId != null && systemId.endsWith(DTD_EXTENSION)){
                String dtdname = systemId.substring(systemId.lastIndexOf(DTD_NAME),systemId.lastIndexOf("."));
                Resource resource = new ClassPathResource(dtdname,getClass());
                is = new InputSource(resource.getInputStream());
                is.setPublicId(publicId);
                is.setSystemId(systemId);
                return is;
            }
            return null;
        }

    }
}


