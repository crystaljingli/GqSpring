package org.gq.spring.mvc.service;

public interface UserServiceI {
    public String query(String id);
    public String queryByAge(String id,Integer age);
}
