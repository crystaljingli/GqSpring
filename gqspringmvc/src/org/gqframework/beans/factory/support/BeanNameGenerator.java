package org.gqframework.beans.factory.support;

import org.gqframework.beans.factory.config.BeanDefinition;

public interface BeanNameGenerator {
    public String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry registry);
}
