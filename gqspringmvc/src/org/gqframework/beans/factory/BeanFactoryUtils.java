package org.gqframework.beans.factory;

import org.gq.spring.ioc.BeanFactory;
import org.gqframework.util.Assert;

public abstract class BeanFactoryUtils {
    /**
     * 用于生成bean名称的分隔符;
     */
    public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";
    /**
     * 工厂bean前缀
     */
    public static final String FACTORY_BEAN_PREFIX = "&";

    /**
     * 转换bean name
     * @param name
     * @return
     */
    public static String transformedBeanName(String name) {
        Assert.notNull(name, "'name' must not be null");
        String beanName = name;
        while (beanName.startsWith(FACTORY_BEAN_PREFIX)) {
            beanName = beanName.substring(FACTORY_BEAN_PREFIX.length());
        }
        return beanName;
    }
}
