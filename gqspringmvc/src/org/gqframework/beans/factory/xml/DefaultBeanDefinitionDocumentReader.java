package org.gqframework.beans.factory.xml;

import org.gqframework.beans.factory.BeanDefinitionStoreException;
import org.gqframework.beans.factory.config.BeanDefinitionHolder;
import org.gqframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.gqframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.gqframework.core.io.Resource;
import org.gqframework.core.io.support.ResourcePatternResolver;
import org.gqframework.core.io.support.ResourcePatternUtils;
import org.gqframework.lang.Nullable;
import org.gqframework.util.ResourceUtils;
import org.gqframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 默认bean加载器
 */
public class DefaultBeanDefinitionDocumentReader
        implements  BeanDefinitionDocumentReader{

    private XmlReaderContext readerContext;

    public XmlReaderContext getReaderContext() {
        return readerContext;
    }

    /**
     *<import resource="*****"></import>
     * 引入其他xml配置
     */
    public static final String IMPORT_ELEMENT = "import";
    public static final String RESOURCE_ATTRIBUTE = "resource";

    public static final String ALIAS_ELEMENT = "alias";
    public static final String ALIAS_ATTRIBUTE = "alias";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String BEAN_ELEMENT = "bean";
    public static final String NESTED_BEANS_ELEMENT = "beans";

    @Nullable
    private BeanDefinitionParserDelegate delegate;

    /**
     * 注册bean定义
     * @param doc the DOM document
     * @param readerContext the current context of the reader
     * (includes the target registry and the resource being parsed)
     * @throws BeanDefinitionStoreException
     */
    @Override
    public void registerBeanDefinitions(Document doc
            , XmlReaderContext readerContext) throws BeanDefinitionStoreException {
        this.readerContext = readerContext;
        Element root = doc.getDocumentElement();
        doRegisterBeanDefinitions(root);
    }

    /**
     * 创建解析代理，同时初始化默认bean定义
     * @param readerContext
     * @param root
     * @param parentDelegate
     * @return
     */
    protected BeanDefinitionParserDelegate createDelegate(
            XmlReaderContext readerContext, Element root, @Nullable BeanDefinitionParserDelegate parentDelegate) {

        BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate(readerContext);
        //注册默认的bean定义,这些定义在嵌套的情况会继承
        delegate.initDefaults(root, parentDelegate);
        return delegate;
    }

    protected void doRegisterBeanDefinitions(Element root){
            //此处可能会被嵌套的beans递归调用
            //现将this.delegate设置成父代理
            //最后解析完后恢复this.delegate
            BeanDefinitionParserDelegate parent = this.delegate;
            this.delegate = createDelegate(getReaderContext(),root,parent);
            if(this.delegate.isDefaultNamespace(root)){
                //此处是设置生产环境
//                String profileSpec = root.getAttribute(PROFILE_ATTRIBUTE);
//                if (StringUtils.hasText(profileSpec)) {
//                    String[] specifiedProfiles = StringUtils.tokenizeToStringArray(
//                            profileSpec, BeanDefinitionParserDelegate.MULTI_VALUE_ATTRIBUTE_DELIMITERS);
//                    if (!getReaderContext().getEnvironment().acceptsProfiles(specifiedProfiles)) {
//                        if (logger.isInfoEnabled()) {
//                            logger.info("Skipped XML bean definition file due to specified profiles [" + profileSpec +
//                                    "] not matching: " + getReaderContext().getResource());
//                        }
//                        return;
//                    }
//                }
            }

            preProcessXml(root);
            parseBeanDefinitions(root, this.delegate);
            postProcessXml(root);

            this.delegate = parent;
    }

    /**
     * 解析bean定义
     * @param root
     * @param delegate
     */
    protected void parseBeanDefinitions(Element root , BeanDefinitionParserDelegate delegate){
        if(delegate.isDefaultNamespace(root)){//默认命名空间
            NodeList nl = root.getChildNodes();
            for(int i=0;i<nl.getLength();i++){
                Node node = nl.item(i);
                if(node instanceof Element){
                    Element ele = (Element)node;
                    if(delegate.isDefaultNamespace(ele)){
                        parseDefaultElement(ele,delegate);
                    }else{
                        delegate.parseCustomElement(ele);
                    }
                }
            }
        }else{
            delegate.parseCustomElement(root);
        }
    }

    /**
     * 解析默认元素
     * @param ele
     * @param delegate
     */
    private void parseDefaultElement(Element ele
            ,BeanDefinitionParserDelegate delegate){
        if(delegate.nodeNameEquals(ele,IMPORT_ELEMENT)){
            importBeanDefinitionResource(ele);
        }else if(delegate.nodeNameEquals(ele,ALIAS_ELEMENT)){
            processAliasRegistration(ele);
        }else if(delegate.nodeNameEquals(ele,BEAN_ELEMENT)){
            processBeanDefinition(ele,delegate);
        }else if(delegate.nodeNameEquals(ele,NESTED_BEANS_ELEMENT)){
            doRegisterBeanDefinitions(ele);
        }
    }

    /**
     * 解析自定义元素
     * @param ele
     */
    private void parseCustomElement(Element ele){

    }
    /**
     * 解析input元素
     */
    protected void importBeanDefinitionResource(Element ele){
        String location = ele.getAttribute(RESOURCE_ATTRIBUTE);

        // 解析系统属性: e.g. "${user.dir}"
        //location = getReaderContext().getEnvironment().resolveRequiredPlaceholders(location);

        Set<Resource> actualResources = new LinkedHashSet<>();
        boolean absoluteLocation = false;
        try{
            //是否是绝对路径
            absoluteLocation = ResourcePatternUtils.isUrl(location) ||
                    ResourceUtils.toURI(location).isAbsolute();
        }catch (Exception e){

        }

        if(absoluteLocation){
            try{
                int importCount = this.getReaderContext()
                        .getReader().loadBeanDefinitions(location,actualResources);

            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            try{
                int importCount;
                Resource relativeResource =
                        getReaderContext().getResource().createRelative(location);
                if(relativeResource.exists()){
                    importCount = getReaderContext().getReader().loadBeanDefinitions(relativeResource);
                    actualResources.add(relativeResource);
                }else{
                    String baseLocation = getReaderContext().getResource().getURL().toString();
                    importCount = getReaderContext().getReader()
                            .loadBeanDefinitions(
                                    StringUtils.applyRelativePath(baseLocation,location),actualResources
                            );
                }
                Resource[] actResArray = actualResources.toArray(new Resource[actualResources.size()]);
                //getReaderContext().fireImportProcessed(location,actResArray,extractSource(ele));

            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }

    protected void processAliasRegistration(Element ele){
        String name = ele.getAttribute(NAME_ATTRIBUTE);
        String alias = ele.getAttribute(ALIAS_ATTRIBUTE);

        boolean valid = true;

        if(!StringUtils.hasText(name)){

            valid = false;
        }

        if(!StringUtils.hasText(alias)){

            valid = false;
        }

        if(valid){
            try{
                getReaderContext().getReader().getRegistry()
                        .registerAlias(name,alias);
            }catch(Exception e){
                e.printStackTrace();
            }
            //getReaderContext().fireAliasRegistered(name, alias, extractSource(ele));
        }
    }

    protected  void processBeanDefinition(Element ele,BeanDefinitionParserDelegate delegate){
        BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
        //TODO:注册bean definition到register;

        BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
    }

    /**
     * 解析xml之前调用
     * @param root
     */
    protected void preProcessXml(Element root) {
    }

    /**
     * 解析xml之后调用
     * @param root
     */
    protected void postProcessXml(Element root) {
    }
}
