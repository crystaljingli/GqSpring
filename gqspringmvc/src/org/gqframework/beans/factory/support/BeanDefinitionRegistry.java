package org.gqframework.beans.factory.support;

import org.gqframework.beans.factory.BeanDefinitionStoreException;
import org.gqframework.beans.factory.NoSuchBeanDefinitionException;
import org.gqframework.beans.factory.config.BeanDefinition;
import org.gqframework.core.AliasRegistry;

public interface BeanDefinitionRegistry extends AliasRegistry {
    void registerBeanDefinition(String beanName,
                                BeanDefinition beanDefinition) throws BeanDefinitionStoreException;

    void removeBeanDefinition(String beanname) throws NoSuchBeanDefinitionException;

    BeanDefinition getBeanDefinition(String beanname) throws NoSuchBeanDefinitionException;

    boolean containsBeanDefinition(String beanname);

    String[] getBeanDefinitionNames();

    int getBeanDefinitionCount();

    boolean isBeanNameInUse(String name);
}
