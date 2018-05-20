package org.gqframework.beans.factory.support;

/**
 * bean定义中用到的常量
 */
public interface BeanDefinitionConstant {

    /**
     * 默认空间
     */
    public static final String SCOPE_DEFAULT = "";

    /**
     * 自动装备 no
     */
    public static final int AUTOWIRE_NO = 0;

    /**
     * 自动装备 名称
     */
    public static final int AUTOWIRE_BY_NAME = 1;

    /**
     * 自动装备 类型
     */
    public static final int AUTOWIRE_BY_TYPE = 2;

    /**
     * 自动装备 构造器
     */
    public static final int AUTOWIRE_CONSTRUCTOR = 3;

    @Deprecated
    public static final int AUTOWIRE_AUTODETECT = 4;
}
