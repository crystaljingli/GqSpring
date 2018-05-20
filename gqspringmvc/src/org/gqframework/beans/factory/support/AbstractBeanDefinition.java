package org.gqframework.beans.factory.support;

import org.gqframework.beans.MutablePropertyValues;
import org.gqframework.beans.factory.config.BeanDefinition;
import org.gqframework.beans.factory.config.ConstructorArgumentValues;
import org.gqframework.lang.Nullable;

import static org.gqframework.beans.factory.support.BeanDefinitionConstant.AUTOWIRE_NO;

public abstract class AbstractBeanDefinition  implements  BeanDefinition{
    @Nullable
    private volatile Object beanClass;

    private int autowireMode = AUTOWIRE_NO;
    private String initMethodName;
    //是否执行默认初始化方法init()
    private boolean enforceInitMethod = true;

    @Nullable
    private String destroyMethodName;
    //是否执行默认销毁方法init()
    private boolean enforceDestroyMethod = true;

    private String description;
    private String beanClassName;

    protected AbstractBeanDefinition(){

    }

    protected AbstractBeanDefinition(BeanDefinition original) {
//        setParentName(original.getParentName());
//        setBeanClassName(original.getBeanClassName());
//        setScope(original.getScope());
//        setAbstract(original.isAbstract());
//        setLazyInit(original.isLazyInit());
//        setFactoryBeanName(original.getFactoryBeanName());
//        setFactoryMethodName(original.getFactoryMethodName());
//        this.constructorArgumentValues = new ConstructorArgumentValues(original.getConstructorArgumentValues());
//        this.propertyValues = new MutablePropertyValues(original.getPropertyValues());
//        setRole(original.getRole());
//        setSource(original.getSource());
//        copyAttributesFrom(original);
//
//        if (original instanceof AbstractBeanDefinition) {
//            AbstractBeanDefinition originalAbd = (AbstractBeanDefinition) original;
//            if (originalAbd.hasBeanClass()) {
//                setBeanClass(originalAbd.getBeanClass());
//            }
//            setAutowireMode(originalAbd.getAutowireMode());
//            setDependencyCheck(originalAbd.getDependencyCheck());
//            setDependsOn(originalAbd.getDependsOn());
//            setAutowireCandidate(originalAbd.isAutowireCandidate());
//            setPrimary(originalAbd.isPrimary());
//            copyQualifiersFrom(originalAbd);
//            setInstanceSupplier(originalAbd.getInstanceSupplier());
//            setNonPublicAccessAllowed(originalAbd.isNonPublicAccessAllowed());
//            setLenientConstructorResolution(originalAbd.isLenientConstructorResolution());
//            setMethodOverrides(new MethodOverrides(originalAbd.getMethodOverrides()));
//            setInitMethodName(originalAbd.getInitMethodName());
//            setEnforceInitMethod(originalAbd.isEnforceInitMethod());
//            setDestroyMethodName(originalAbd.getDestroyMethodName());
//            setEnforceDestroyMethod(originalAbd.isEnforceDestroyMethod());
//            setSynthetic(originalAbd.isSynthetic());
//            setResource(originalAbd.getResource());
//        }
//        else {
//            setResourceDescription(original.getResourceDescription());
//        }
    }

    public void setAutowireMode(int autowireMode) {
        this.autowireMode = autowireMode;
    }

    /**
     * Return the autowire mode as specified in the bean definition.
     */
    public int getAutowireMode() {
        return this.autowireMode;
    }


    /**
     * 克隆bean定义
     * @return
     */
    public abstract AbstractBeanDefinition cloneBeanDefinition();

    @Override
    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    @Override
    public String getBeanClassName() {
        return this.beanClassName;
    }

    @Override
    public void setScope(String scope) {

    }

    @Override
    public String getScope() {
        return null;
    }

    @Override
    public void setLazyInit(boolean lazyInit) {

    }

    @Override
    public boolean isLazyInit() {
        return false;
    }

    @Override
    public void setDependsOn(String... dependsOn) {

    }

    @Override
    public String[] getDependsOn() {
        return new String[0];
    }

    @Override
    public void setAutowireCandidate(boolean autowireCandidate) {

    }

    @Override
    public boolean isAutowireCandidate() {
        return false;
    }

    @Override
    public void setPrimary(boolean primary) {

    }

    @Override
    public boolean isPrimary() {
        return false;
    }

    @Override
    public void setFactoryBeanName(String factoryBeanName) {

    }

    @Override
    public String getFactoryBeanName() {
        return null;
    }

    @Override
    public void setFactoryMethodName(String factoryMethodName) {

    }

    @Override
    public String getFactoryMethodName() {
        return null;
    }

    @Override
    public ConstructorArgumentValues getConstructorArgumentValues() {
        return null;
    }

    @Override
    public MutablePropertyValues getPropertyValues() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public boolean isPrototype() {
        return false;
    }

    @Override
    public boolean isAbstract() {
        return false;
    }

    @Override
    public int getRole() {
        return 0;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getResourceDescription() {
        return null;
    }

    @Override
    public BeanDefinition getOriginatingBeanDefinition() {
        return null;
    }

    @Override
    public Object getSource() {
        return null;
    }

    @Override
    public void setAttribute(String name, Object value) {

    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public Object removeAttribute(String name) {
        return null;
    }

    @Override
    public boolean hasAttribute(String name) {
        return false;
    }

    @Override
    public String[] attributeNames() {
        return new String[0];
    }

    /**
     * Specify the class for this bean.
     */
    public void setBeanClass(@Nullable Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public void setInitMethodName(String initMethodName){
        this.initMethodName = initMethodName;
    }

    /**
     *
     * @param enforceInitMethod
     */
    public void setEnforceInitMethod(boolean enforceInitMethod) {
        this.enforceInitMethod = enforceInitMethod;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public boolean isEnforceInitMethod() {
        return enforceInitMethod;
    }

    @Nullable
    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(@Nullable String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    public boolean isEnforceDestroyMethod() {
        return enforceDestroyMethod;
    }

    public void setEnforceDestroyMethod(boolean enforceDestroyMethod) {
        this.enforceDestroyMethod = enforceDestroyMethod;
    }

    public void setDescription(String description){
        this.description  = description;
    }

    /**
     * Return the class of the wrapped bean, if already resolved.
     * @return the bean class, or {@code null} if none defined
     * @throws IllegalStateException if the bean definition does not define a bean class,
     * or a specified bean class name has not been resolved into an actual Class
     */
    public Class<?> getBeanClass() throws IllegalStateException {
        Object beanClassObject = this.beanClass;
        if (beanClassObject == null) {
            throw new IllegalStateException("No bean class specified on bean definition");
        }
        if (!(beanClassObject instanceof Class)) {
            throw new IllegalStateException(
                    "Bean class name [" + beanClassObject + "] has not been resolved into an actual Class");
        }
        return (Class<?>) beanClassObject;
    }

    public void setAbstract(boolean abstractFlag) {
        this.abstractFlag = abstractFlag;
    }

    private boolean abstractFlag = false;
}
