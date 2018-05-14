package org.gqframework.beans.factory.xml;

import org.gqframework.beans.factory.config.BeanDefinition;
import org.gqframework.beans.factory.config.BeanDefinitionHolder;
import org.gqframework.lang.Nullable;
import org.gqframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BeanDefinitionParserDelegate {

    public static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";

    public static final String ID_ATTRIBUTE = "id";
    public static final String NAME_ATTRIBUTE = "name";

    public static final String MULTI_VALUE_ATTRIBUTE_DELIMITERS = ",; ";

    public boolean isDefaultNamespace(Node node) {
        return isDefaultNamespace(getNamespaceURI(node));
    }

    public boolean isDefaultNamespace(@Nullable String namespaceUri) {
        return (!StringUtils.hasLength(namespaceUri) ||
                    BEANS_NAMESPACE_URI.equals(namespaceUri));
    }

    public String getNamespaceURI(Node node){
        return node.getNamespaceURI();
    }

    public BeanDefinition parseCustomElement(Element ele){
        return parseCustomElement(ele, null);
    }

    public BeanDefinition parseCustomElement(Element ele
            , @Nullable BeanDefinition containingBd){
        String namespaceUri = getNamespaceURI(ele);
        if(namespaceUri == null){
            return null;
        }

        //Name
        return parseCustomElement(ele, null);
    }

    public boolean nodeNameEquals(Node node,String nodeName){
        return nodeName.equals(
                node.getNodeName()
        ) || nodeName.equals(node.getLocalName());
    }
    @Nullable
    public BeanDefinitionHolder parseBeanDefinitionElement(Element ele){

        return parseBeanDefinitionElement(ele,null);
    }
    @Nullable
    public BeanDefinitionHolder parseBeanDefinitionElement(Element ele
            , @Nullable BeanDefinition containingBean) {
        String id = ele.getAttribute(ID_ATTRIBUTE);
        String nameAttr = ele.getAttribute(NAME_ATTRIBUTE);

        List<String> aliases = new ArrayList<>();
        if(StringUtils.hasLength(nameAttr)){
            String[] nameArr = StringUtils.tokenizeToStringArray(
                    nameAttr,MULTI_VALUE_ATTRIBUTE_DELIMITERS);
            aliases.addAll(Arrays.asList(nameArr));
        }
        String beanName = id;

        if(!StringUtils.hasText(beanName)&& !aliases.isEmpty()){
            beanName = aliases.remove(0);
        }

        if(containingBean == null){
            //checkNameUniqueness(beanName, aliases, ele);
        }

        AbstractBeanDefinition beanDefinition = parseBeanDefinitionElement(ele, beanName, containingBean);
        if (beanDefinition != null) {
            if (!StringUtils.hasText(beanName)) {
                try {
                    if (containingBean != null) {
                        beanName = BeanDefinitionReaderUtils.generateBeanName(
                                beanDefinition, this.readerContext.getRegistry(), true);
                    }
                    else {
                        beanName = this.readerContext.generateBeanName(beanDefinition);
                        // Register an alias for the plain bean class name, if still possible,
                        // if the generator returned the class name plus a suffix.
                        // This is expected for Spring 1.2/2.0 backwards compatibility.
                        String beanClassName = beanDefinition.getBeanClassName();
                        if (beanClassName != null &&
                                beanName.startsWith(beanClassName) && beanName.length() > beanClassName.length() &&
                                !this.readerContext.getRegistry().isBeanNameInUse(beanClassName)) {
                            aliases.add(beanClassName);
                        }
                    }
                    if (logger.isDebugEnabled()) {
                        logger.debug("Neither XML 'id' nor 'name' specified - " +
                                "using generated bean name [" + beanName + "]");
                    }
                }
                catch (Exception ex) {
                    error(ex.getMessage(), ele);
                    return null;
                }
            }
            String[] aliasesArray = StringUtils.toStringArray(aliases);
            return new BeanDefinitionHolder(beanDefinition, beanName, aliasesArray);
        }

        return null;
    }
}
