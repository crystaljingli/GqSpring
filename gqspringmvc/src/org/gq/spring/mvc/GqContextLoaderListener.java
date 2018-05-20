package org.gq.spring.mvc;

import org.gqframework.util.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 容器启动加载器
 */
public class GqContextLoaderListener implements ServletContextListener {

    private final static String CONTEXT_CONFIG_LOCATION_PARAM = "contextConfigLocation";
    private final static String DEFAULT_CONTEXT_CONFIG_LOCATION = "classpath:applicationContext.xml";

    //ioc容器xml配置路径
    private String contextConfigLocation;

    public String getContextConfigLocation() {
        return contextConfigLocation;
    }

    public void setContextConfigLocation(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
    }

    /**
     * 容器初始化
     * @param servletContextEvent
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context  = servletContextEvent.getServletContext();
        contextConfigLocation = context.getInitParameter(CONTEXT_CONFIG_LOCATION_PARAM);
        if(StringUtils.isEmpty(contextConfigLocation)){//没有配置默认容器路径
            contextConfigLocation = DEFAULT_CONTEXT_CONFIG_LOCATION;
        }

        //创建IOC容器
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
