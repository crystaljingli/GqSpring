package org.gqframework.beans.factory.xml;

import org.gqframework.beans.factory.BeanDefinitionStoreException;
import org.gqframework.beans.factory.NoSuchBeanDefinitionException;
import org.gqframework.beans.factory.config.BeanDefinition;
import org.gqframework.beans.factory.support.BeanDefinitionRegistry;

public class XmlBeanFactory implements BeanDefinitionRegistry{

    private XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this);


    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {

    }

    @Override
    public void removeBeanDefinition(String beanname) throws NoSuchBeanDefinitionException {

    }

    @Override
    public BeanDefinition getBeanDefinition(String beanname) throws NoSuchBeanDefinitionException {
        return null;
    }

    @Override
    public boolean containsBeanDefinition(String beanname) {
        return false;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return new String[0];
    }

    @Override
    public int getBeanDefinitionCount() {
        return 0;
    }

    @Override
    public boolean isBeanNameInUse(String name) {
        return false;
    }

    @Override
    public void registerAlias(String name, String alias) {

    }

    @Override
    public void removeAlias(String alias) {

    }

    @Override
    public boolean isAlias(String name) {
        return false;
    }

    @Override
    public String[] getAlias(String name) {
        return new String[0];
    }
}
