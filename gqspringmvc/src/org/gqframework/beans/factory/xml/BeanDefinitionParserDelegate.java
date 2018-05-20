package org.gqframework.beans.factory.xml;

import org.gqframework.beans.factory.config.BeanDefinition;
import org.gqframework.beans.factory.config.BeanDefinitionHolder;
import org.gqframework.beans.factory.parsing.BeanEntry;
import org.gqframework.beans.factory.parsing.ParseState;
import org.gqframework.beans.factory.support.AbstractBeanDefinition;
import org.gqframework.beans.factory.support.BeanDefinitionConstant;
import org.gqframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.gqframework.lang.Nullable;
import org.gqframework.util.Assert;
import org.gqframework.util.PatternMatchUtils;
import org.gqframework.util.StringUtils;
import org.gqframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BeanDefinitionParserDelegate {

    public static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";

    public static final String VALUE_TRUE = "true";
    public static final String VALUE_FALSE = "false";
    public static final String  VALUE_DEFAULT = "default";

    public static final String VALUE_AUTOWIRE_NO = "no";

    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_NAME = "name";
    public static final String ATTRIBUTE_CLASS = "class";
    public static final String ATTRIBUTE_PARENT = "parent";
    public static final String ATTRIBUTE_SINGLETON = "singleton";
    public static final String ATTRIBUTE_SCOPE = "scope";
    public static final String  ATTRIBUTE_ABSTRACT = "abstract";
    public static final String  ATTRIBUTE_LAZY_INIT = "lazy_init";
    public static final String  ATTRIBUTE_AUTOWIRE = "autowire";

    public static final String ATTRIBUTE_AUTOWIRE_CANDIDATE = "autowire-candidate";
    public static final String ATTRIBUTE_PRIMARY = "primary";
    public static final String ATTRIBUTE_DEPENDS_ON = "depends-on";
    public static final String ATTRIBUTE_INIT_METHOD = "init-method";
    public static final String ATTRIBUTE_DESTROY_METHOD = "destroy-method";
    public static final String ATTRIBUTE_FACTORY_METHOD = "factory-method";
    public static final String ATTRIBUTE_FACTORY_BEAN = "factory-bean";

    public static final String MULTI_VALUE_ATTRIBUTE_DELIMITERS = ",; ";

    public static final String  ATTRIBUTE_DEFAULT_LAZY_INIT = "default_lazy_init";
    public static final String  ATTRIBUTE_DEFAULT_MERGE = "default-merge";
    public static final String  ATTRIBUTE_DEFAULT_AUTOWIRE = "default-autowire";
    public static final String  ATTRIBUTE_DEFAULT_AUTOWIRE_CANDIDATES = "default-autowire-candidates";
    public static final String  ATTRIBUTE_DEFAULT_INIT_METHOD = "default-init-method";
    public static final String ATTRIBUTE_DEFAULT_DESTROY_METHOD = "default-destroy-method";

    public static final String AUTOWIRE_NO_VALUE = "no";
    public static final String AUTOWIRE_BY_NAME_VALUE = "byName";
    public static final String AUTOWIRE_BY_TYPE_VALUE = "byType";
    public static final String AUTOWIRE_CONSTRUCTOR_VALUE = "constructor";
    public static final String AUTOWIRE_AUTODETECT_VALUE = "autodetect";

    public static final String ELEMENT_DESCRIPTION = "description";

    private XmlReaderContext readerContext;

    private final DocumentDefaultsDefinition defaults = new DocumentDefaultsDefinition();

    private final ParseState parseState = new ParseState();

    public BeanDefinitionParserDelegate(XmlReaderContext readerContext){
        Assert.notNull(readerContext, "XmlReaderContext must not be null");
        this.readerContext = readerContext;
    }

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
        //return parseCustomElement(ele, null);
        return null;
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
        String id = ele.getAttribute(ATTRIBUTE_ID);
        String nameAttr = ele.getAttribute(ATTRIBUTE_NAME);

        List<String> aliases = new ArrayList<>();
        //name属性 指定bean别名 别名可以指定多个，用，；分割均可
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
//                    if (logger.isDebugEnabled()) {
//                        logger.debug("Neither XML 'id' nor 'name' specified - " +
//                                "using generated bean name [" + beanName + "]");
//                    }
                }
                catch (Exception ex) {
                    //error(ex.getMessage(), ele);
                    return null;
                }
            }
            String[] aliasesArray = StringUtils.toStringArray(aliases);
            return new BeanDefinitionHolder(beanDefinition, beanName, aliasesArray);
        }

        return null;
    }

    @Nullable
    public AbstractBeanDefinition parseBeanDefinitionElement(
            Element ele, String beanName, @Nullable BeanDefinition containingBean) {

        this.parseState.push(new BeanEntry(beanName));

        String className = null;
        //class属性
        if (ele.hasAttribute(ATTRIBUTE_CLASS)) {
            className = ele.getAttribute(ATTRIBUTE_CLASS).trim();
        }
        //父bean
        String parent = null;
        if (ele.hasAttribute(ATTRIBUTE_PARENT)) {
            parent = ele.getAttribute(ATTRIBUTE_PARENT);
        }

        try {
            //创建默认bean定义
            AbstractBeanDefinition bd = createBeanDefinition(className, parent);
            //设置bean 属性
            parseBeanDefinitionAttributes(ele, beanName, containingBean, bd);
            bd.setDescription(DomUtils.getChildElementValueByTagName(ele, ELEMENT_DESCRIPTION));

//            parseMetaElements(ele, bd);
//            parseLookupOverrideSubElements(ele, bd.getMethodOverrides());
//            parseReplacedMethodSubElements(ele, bd.getMethodOverrides());
//
//            parseConstructorArgElements(ele, bd);
//            parsePropertyElements(ele, bd);
//            parseQualifierElements(ele, bd);
//
//            bd.setResource(this.readerContext.getResource());
//            bd.setSource(extractSource(ele));

            return bd;
        }
        catch (ClassNotFoundException ex) {
            //error("Bean class [" + className + "] not found", ele, ex);
        }
        catch (NoClassDefFoundError err) {
           // error("Class that bean class [" + className + "] depends on not found", ele, err);
        }
        catch (Throwable ex) {
            //error("Unexpected failure during bean definition parsing", ele, ex);
        }
        finally {
            this.parseState.pop();
        }

        return null;
    }

    protected AbstractBeanDefinition createBeanDefinition(@Nullable String className
            ,@Nullable String parentName) throws ClassNotFoundException {
            return BeanDefinitionReaderUtils.createBeanDefinition(
                    parentName,className,this.readerContext.getBeanClassLoader()
            );
    }

    public AbstractBeanDefinition parseBeanDefinitionAttributes(Element ele,
                                                                String beanName,
                                                                @Nullable BeanDefinition containingBean,
                                                                AbstractBeanDefinition bd) {
        //作废
        if(ele.hasAttribute(ATTRIBUTE_SINGLETON)){

        }
        //bean的空间
        if(ele.hasAttribute(ATTRIBUTE_SCOPE)){
            bd.setScope(ele.getAttribute(ATTRIBUTE_SCOPE));
        }else if(containingBean != null){
            bd.setScope(containingBean.getScope());
        }
        //抽象bean,属性可以被继承
        if(ele.hasAttribute(ATTRIBUTE_ABSTRACT)){
            bd.setAbstract(VALUE_TRUE.equals(ele.getAttribute(ATTRIBUTE_ABSTRACT)));
        }
        //延迟加载
        String lazyInit = ele.getAttribute(ATTRIBUTE_LAZY_INIT);
        if(VALUE_DEFAULT.equals(lazyInit)){
            lazyInit = this.defaults.getLazyInit();
        }
        bd.setLazyInit(VALUE_TRUE.equals(lazyInit));
        //自动装配
        String autowired = ele.getAttribute(ATTRIBUTE_AUTOWIRE);
        bd.setAutowireMode(getAutowiredMode(autowired));

        //前置依赖bean ,什么情况下使用：一个bean并不是直接依赖另一个bean，但是需要另一个bean优先初始化时使用
        if(ele.hasAttribute(ATTRIBUTE_DEPENDS_ON)){
            String dependsOn = ele.getAttribute(ATTRIBUTE_DEPENDS_ON);
            bd.setDependsOn(StringUtils.tokenizeToStringArray(dependsOn, MULTI_VALUE_ATTRIBUTE_DELIMITERS));
        }
        /*
            是否是自动装备候选bean。
            beans上面可以配置通配符，如果bean上面没有配置或者默认的，将会通过beans上面指定的规则去匹配
         */
        String autowireCandidate = ele.getAttribute(ATTRIBUTE_AUTOWIRE_CANDIDATE);
        if ("".equals(autowireCandidate) || VALUE_DEFAULT.equals(autowireCandidate)) {
            String candidatePattern = this.defaults.getAutowireCandidates();
            if (candidatePattern != null) {
                String[] patterns = StringUtils.commaDelimitedListToStringArray(candidatePattern);
                bd.setAutowireCandidate(PatternMatchUtils.simpleMatch(patterns, beanName));
            }
        }else {
            bd.setAutowireCandidate(VALUE_TRUE.equals(autowireCandidate));
        }
        //依赖类型的注入方式使用时，配置了这个标识的将作为首选项
        if (ele.hasAttribute(ATTRIBUTE_PRIMARY)) {
            bd.setPrimary(VALUE_TRUE.equals(ele.getAttribute(ATTRIBUTE_PRIMARY)));
        }
        //bean上面有指定init-method时，既会调用默认的init方法，也会调用新指定的初始化方法
        //如果beans有指定默认初始化方法，则默认的init方法不会在调用
        if (ele.hasAttribute(ATTRIBUTE_INIT_METHOD)) {
            String initMethodName = ele.getAttribute(ATTRIBUTE_INIT_METHOD);
            if (!"".equals(initMethodName)) {
                bd.setInitMethodName(initMethodName);
            }
        }else if (this.defaults.getInitMethod() != null) {
            bd.setInitMethodName(this.defaults.getInitMethod());
            bd.setEnforceInitMethod(false);
        }

        if (ele.hasAttribute(ATTRIBUTE_DESTROY_METHOD)) {
            String destroyMethodName = ele.getAttribute(ATTRIBUTE_DESTROY_METHOD);
            bd.setDestroyMethodName(destroyMethodName);
        }else if (this.defaults.getDestroyMethod() != null) {
            bd.setDestroyMethodName(this.defaults.getDestroyMethod());
            bd.setEnforceDestroyMethod(false);
        }
        //通过factory-method和factory-bean来实现工厂bean和工厂方法生成bean
        if (ele.hasAttribute(ATTRIBUTE_FACTORY_METHOD)) {
            bd.setFactoryMethodName(ele.getAttribute(ATTRIBUTE_FACTORY_METHOD));
        }
        if (ele.hasAttribute(ATTRIBUTE_FACTORY_BEAN)) {
            bd.setFactoryBeanName(ele.getAttribute(ATTRIBUTE_FACTORY_BEAN));
        }

        return bd;
    }


    /**
     * Initialize the default settings assuming a {@code null} parent delegate.
     */
    public void initDefaults(Element root) {
        initDefaults(root, null);
    }


    public void initDefaults(Element root, @Nullable BeanDefinitionParserDelegate parent) {
        populateDefaults(this.defaults, (parent != null ? parent.defaults : null), root);
        //this.readerContext.fireDefaultsRegistered(this.defaults);
    }

    protected void populateDefaults(DocumentDefaultsDefinition defaults, @Nullable DocumentDefaultsDefinition parentDefaults, Element root) {
        String lazyInit = root.getAttribute(ATTRIBUTE_DEFAULT_LAZY_INIT);
        if (VALUE_DEFAULT.equals(lazyInit)) {
            // Potentially inherited from outer <beans> sections, otherwise falling back to false.
            lazyInit = (parentDefaults != null ? parentDefaults.getLazyInit() : VALUE_FALSE);
        }
        defaults.setLazyInit(lazyInit);
        //集合合并
        String merge = root.getAttribute(ATTRIBUTE_DEFAULT_MERGE);
        if (VALUE_DEFAULT.equals(merge)) {
            // Potentially inherited from outer <beans> sections, otherwise falling back to false.
            merge = (parentDefaults != null ? parentDefaults.getMerge() : VALUE_FALSE);
        }
        defaults.setMerge(merge);

        String autowire = root.getAttribute(ATTRIBUTE_DEFAULT_AUTOWIRE);
        if (VALUE_DEFAULT.equals(autowire)) {
            // Potentially inherited from outer <beans> sections, otherwise falling back to 'no'.
            autowire = (parentDefaults != null ? parentDefaults.getAutowire() : VALUE_AUTOWIRE_NO);
        }
        defaults.setAutowire(autowire);

        if (root.hasAttribute(ATTRIBUTE_DEFAULT_AUTOWIRE_CANDIDATES)) {
            defaults.setAutowireCandidates(root.getAttribute(ATTRIBUTE_DEFAULT_AUTOWIRE_CANDIDATES));
        }
        else if (parentDefaults != null) {
            defaults.setAutowireCandidates(parentDefaults.getAutowireCandidates());
        }

        if (root.hasAttribute(ATTRIBUTE_DEFAULT_INIT_METHOD)) {
            defaults.setInitMethod(root.getAttribute(ATTRIBUTE_DEFAULT_INIT_METHOD));
        }else if (parentDefaults != null) {
            defaults.setInitMethod(parentDefaults.getInitMethod());
        }

        if (root.hasAttribute(ATTRIBUTE_DEFAULT_DESTROY_METHOD)) {
            defaults.setDestroyMethod(root.getAttribute(ATTRIBUTE_DEFAULT_DESTROY_METHOD));
        }else if (parentDefaults != null) {
            defaults.setDestroyMethod(parentDefaults.getDestroyMethod());
        }
        //TODO 设置source
        //defaults.setSource(this.readerContext.extractSource(root));
    }

    protected void error(String message, Element source) {
        this.readerContext.error(message, source, this.parseState.snapshot());
    }

    /**
     * 获取自动装配类型
     * @param attValue
     * @return
     */
    public int getAutowiredMode(String attValue){
        String att = attValue;
        if(VALUE_DEFAULT.equals(att)){
            att = this.defaults.getAutowire();
        }

        int autowire = BeanDefinitionConstant.AUTOWIRE_NO;
        if(AUTOWIRE_BY_NAME_VALUE.equals(autowire)){
            autowire = BeanDefinitionConstant.AUTOWIRE_BY_NAME;
        }

        if(AUTOWIRE_BY_TYPE_VALUE.equals(autowire)){
            autowire = BeanDefinitionConstant.AUTOWIRE_BY_TYPE;
        }

        if(AUTOWIRE_CONSTRUCTOR_VALUE.equals(autowire)){
            autowire = BeanDefinitionConstant.AUTOWIRE_CONSTRUCTOR;
        }

        if(AUTOWIRE_AUTODETECT_VALUE.equals(autowire)){
            autowire = BeanDefinitionConstant.AUTOWIRE_AUTODETECT;
        }
        return  autowire;
    }
}
