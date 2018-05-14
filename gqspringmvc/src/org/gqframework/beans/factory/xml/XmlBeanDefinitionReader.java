package org.gqframework.beans.factory.xml;

import org.apache.log4j.Logger;
import org.gqframework.beans.factory.BeanDefinitionStoreException;
import org.gqframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.gqframework.beans.factory.support.BeanDefinitionRegistry;
import org.gqframework.core.NamedThreadLocal;
import org.gqframework.core.io.ClassPathResource;
import org.gqframework.core.io.Resource;
import org.gqframework.core.io.ResourceLoader;
import org.gqframework.core.io.support.EncodedResource;
import org.gqframework.core.io.support.PathMatchingResourcePatternResolver;
import org.gqframework.core.io.support.ResourcePatternResolver;
import org.gqframework.lang.Nullable;
import org.gqframework.util.Assert;
import org.gqframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * xml bean定义读取
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {
    private final static Logger logger  =  Logger.getLogger(XmlBeanDefinitionReader.class);

    public static final int VALIDATION_NONE = XmlValidationModeDetector.VALIDATION_NONE;
    //自动检测验证模式
    public static final int VALIDATION_AUTO = XmlValidationModeDetector.VALIDATION_AUTO;
    //XSD验证模式
    public static final int VALIDATION_XSD = XmlValidationModeDetector.VALIDATION_XSD;
    //DTD验证模式
    public static final int VALIDATION_DTD = XmlValidationModeDetector.VALIDATION_DTD;

    private int validationMode = VALIDATION_AUTO;

    private final XmlValidationModeDetector validationModeDetector = new XmlValidationModeDetector();
    private final ThreadLocal<Set<EncodedResource>> resourcesCurrentlyBeingLoaded =
            new NamedThreadLocal<>("XML bean definition resources currently being loaded");

    private DocumentLoader documentLoader = new DefaultDocumentLoader();
    private EntityResolver entityResolver ;
    private ResourceLoader resourceLoader;
    @Nullable
    private ClassLoader beanClassLoader;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public int loadBeanDefinitions(Resource resource) {
        return loadBeanDefinitions(new EncodedResource(resource));
    }

    public int loadBeanDefinitions(EncodedResource encodedResource){
        Assert.notNull(encodedResource, "EncodedResource must not be null");
        if (logger.isInfoEnabled()) {
            logger.info("Loading XML bean definitions from " + encodedResource.getResource());
        }

        Set<EncodedResource> currentResources = this.resourcesCurrentlyBeingLoaded.get();
        if (currentResources == null) {
            currentResources = new HashSet<>(4);
            this.resourcesCurrentlyBeingLoaded.set(currentResources);
        }
        if (!currentResources.add(encodedResource)) {
            throw new BeanDefinitionStoreException(
                    "Detected cyclic loading of " + encodedResource + " - check your import definitions!");
        }
        try {
            InputStream inputStream = encodedResource.getResource().getInputStream();
            try {
                InputSource inputSource = new InputSource(inputStream);
                if (encodedResource.getEncoding() != null) {
                    inputSource.setEncoding(encodedResource.getEncoding());
                }
                return doLoadBeanDefinitions(inputSource, encodedResource.getResource());
            }
            finally {
                inputStream.close();
            }
        }
        catch (IOException ex) {
            throw new BeanDefinitionStoreException(
                    "IOException parsing XML document from " + encodedResource.getResource(), ex);
        }
        finally {
            currentResources.remove(encodedResource);
            if (currentResources.isEmpty()) {
                this.resourcesCurrentlyBeingLoaded.remove();
            }
        }
    }

    protected int doLoadBeanDefinitions(InputSource inputSource, Resource resource)
            throws BeanDefinitionStoreException {
        try {
            //加载xml，并生成DOCUMENT文档
            Document doc = doLoadDocument(inputSource, resource);
            return registerBeanDefinitions(doc, resource);
        }
        catch (BeanDefinitionStoreException ex) {
            throw ex;
        }
        catch (SAXParseException ex) {
            throw new XmlBeanDefinitionStoreException(resource.getDescription(),
                    "Line " + ex.getLineNumber() + " in XML document from " + resource + " is invalid", ex);
        }
        catch (SAXException ex) {
            throw new XmlBeanDefinitionStoreException(resource.getDescription(),
                    "XML document from " + resource + " is invalid", ex);
        }
        catch (ParserConfigurationException ex) {
            throw new BeanDefinitionStoreException(resource.getDescription(),
                    "Parser configuration exception parsing XML from " + resource, ex);
        }
        catch (IOException ex) {
            throw new BeanDefinitionStoreException(resource.getDescription(),
                    "IOException parsing XML document from " + resource, ex);
        }
        catch (Throwable ex) {
            throw new BeanDefinitionStoreException(resource.getDescription(),
                    "Unexpected exception parsing XML document from " + resource, ex);
        }
    }

    protected Document doLoadDocument(InputSource inputSource, Resource resource) throws Exception {
//        return this.documentLoader.loadDocument(inputSource, getEntityResolver(), this.errorHandler,
//                getValidationModeForResource(resource), isNamespaceAware());
        return null;
    }

    public int registerBeanDefinitions(Document doc, Resource resource) throws BeanDefinitionStoreException {
//        BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
//        int countBefore = getRegistry().getBeanDefinitionCount();
//        documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
//        return getRegistry().getBeanDefinitionCount() - countBefore;
         return 0;
    }

    protected BeanDefinitionDocumentReader createBeanDefinitionDocumentReader() {
        //return BeanDefinitionDocumentReader.class.cast(BeanUtils.instantiateClass(this.documentReaderClass));
        return null;
    }

    /**
     * 从资源中判断验证模式
     * @param resource
     * @return
     */
    protected int getValidationModeForResource(Resource resource) {
        int validationModeToUse = getValidationMode();
        if (validationModeToUse != VALIDATION_AUTO) {
            return validationModeToUse;
        }
        int detectedMode = detectValidationMode(resource);
        if (detectedMode != VALIDATION_AUTO) {
            return detectedMode;
        }
        // Hmm, we didn't get a clear indication... Let's assume XSD,
        // since apparently no DTD declaration has been found up until
        // detection stopped (before finding the document's root tag).
        return VALIDATION_XSD;
    }

    public int getValidationMode() {
        return this.validationMode;
    }

    public static void main(String args[]){
        XmlBeanFactory beanFactory = new XmlBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(new ClassPathResource("classpath:org/gq/spring/ioc/../spring-mvc.xml"));
    }

    protected int detectValidationMode(Resource resource) {
        if (resource.isOpen()) {
            throw new BeanDefinitionStoreException(
                    "Passed-in Resource [" + resource + "] contains an open stream: " +
                            "cannot determine validation mode automatically. Either pass in a Resource " +
                            "that is able to create fresh streams, or explicitly specify the validationMode " +
                            "on your XmlBeanDefinitionReader instance.");
        }

        InputStream inputStream;
        try {
            inputStream = resource.getInputStream();
        }
        catch (IOException ex) {
            throw new BeanDefinitionStoreException(
                    "Unable to determine validation mode for [" + resource + "]: cannot open InputStream. " +
                            "Did you attempt to load directly from a SAX InputSource without specifying the " +
                            "validationMode on your XmlBeanDefinitionReader instance?", ex);
        }

        try {
            return this.validationModeDetector.detectValidationMode(inputStream);
        }
        catch (IOException ex) {
            throw new BeanDefinitionStoreException("Unable to determine validation mode for [" +
                    resource + "]: an error occurred whilst reading from the InputStream.", ex);
        }
    }

    public EntityResolver getEntityResolver(){
        if(this.entityResolver == null){
            ResourceLoader resourceLoader = getResourceLoader();
            if(resourceLoader != null){
                this.entityResolver = new ResourceEntityResolver(resourceLoader);
            }else{
                this.entityResolver = new DelegatingEntityResolver(getBeanClassLoader());
            }
        }
        return this.entityResolver;
    }

    public ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }

    @Nullable
    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }


}
