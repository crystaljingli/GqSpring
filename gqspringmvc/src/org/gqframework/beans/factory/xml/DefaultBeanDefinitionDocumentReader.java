package org.gqframework.beans.factory.xml;

import org.gqframework.beans.factory.BeanDefinitionStoreException;
import org.gqframework.beans.factory.config.BeanDefinitionHolder;
import org.gqframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.gqframework.core.io.Resource;
import org.gqframework.core.io.support.ResourcePatternResolver;
import org.gqframework.core.io.support.ResourcePatternUtils;
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


    @Override
    public void registerBeanDefinitions(Document doc
            , XmlReaderContext readerContext) throws BeanDefinitionStoreException {
        Element root = doc.getDocumentElement();

        BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate();
        if(delegate.isDefaultNamespace(root)){//默认命名空间
            NodeList nl = root.getChildNodes();
            for(int i=0;i<nl.getLength();i++){
                Node node = nl.item(i);
                if(node instanceof Element){
                    Element ele = (Element)node;
                    if(delegate.isDefaultNamespace(ele)){
                        this.parseDefaultElement(ele,delegate);
                    }else{
                        delegate.parseCustomElement(ele);
                    }
                }
            }
        }else{
            delegate.parseCustomElement(root);
        }

    }

    public void doRegisterBeanDefinitions(Element root){

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

    }
}
