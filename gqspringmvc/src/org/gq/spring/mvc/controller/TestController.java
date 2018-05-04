package org.gq.spring.mvc.controller;

import org.gq.spring.mvc.service.UserServiceI;
import org.gq.spring.tag.Autowired;
import org.gq.spring.tag.Controller;
import org.gq.spring.tag.RequestMapping;
import org.gq.spring.tag.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("test")
public class TestController {

    @Autowired
    private UserServiceI service;

    public UserServiceI getService() {
        return service;
    }

    public void setService(UserServiceI service) {
        this.service = service;
    }

    @RequestMapping("/query.do")
    public String query(@RequestParam("id") String id
            , HttpServletRequest request, HttpServletResponse response){
        request.setAttribute("user",service.query(id));
        return "query";
    }

    @RequestMapping("/queryByAge.do")
    public String query(@RequestParam("id") String id,@RequestParam("age") Integer age
            ,HttpServletRequest request, HttpServletResponse response){
        request.setAttribute("user",service.queryByAge(id, age));
        return "query";
    }
}
