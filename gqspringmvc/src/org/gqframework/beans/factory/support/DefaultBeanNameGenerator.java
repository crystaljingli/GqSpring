package org.gqframework.beans.factory.support;

import org.gqframework.beans.factory.config.BeanDefinition;

public class DefaultBeanNameGenerator implements BeanNameGenerator{
    @Override
    public String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry registry) {
        return BeanDefinitionReaderUtils.generateBeanName(beanDefinition,registry);
    }
}
