package org.gq.spring.mvc.handler;

import java.lang.reflect.Method;

/**
 * 记录handlerMapping的相关信息
 */
public class HandlerMapping {
    private String uri;
    private Object bean;//对应的controller
    private Method excutor;//对应的方法

    public HandlerMapping(String uri, Object bean, Method excutor) {
        this.uri = uri;
        this.bean = bean;
        this.excutor = excutor;
    }

    public HandlerMapping() {
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getExcutor() {
        return excutor;
    }

    public void setExcutor(Method excutor) {
        this.excutor = excutor;
    }
}
