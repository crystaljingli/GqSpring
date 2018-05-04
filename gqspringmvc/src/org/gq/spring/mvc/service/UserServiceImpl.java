package org.gq.spring.mvc.service;

import org.gq.spring.tag.Service;

@Service
public class UserServiceImpl implements UserServiceI{
    @Override
    public String query(String id) {
        return "查到用户："+id;
    }
    public String queryByAge(String id,Integer age) {
        return "查到用户："+id + ",年龄=="+age;
    }
}
