package org.gq.spring.mvc;

import org.gq.spring.mvc.handler.HandlerMapping;
import org.gq.spring.tag.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GqDispatchServlet extends HttpServlet {

    private static Map<String,String> initLoadParam;
    private static final Map<String,String> beanNameCache  = new HashMap<String,String>();
    private static final Map<String,Object> beanCache = new HashMap<String,Object>();
    private static final Map<String,HandlerMapping> handleMapping = new HashMap<>();

    private static final String VIEW_PREFIX = "/WEB-INF/jsp/";
    private static final String VIEW_SUFFIX = ".jsp";

    private static final String[] COMMON_TYPE_STRING = new String[]{
            "int","float","double","short","long","char","byte"
    };

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req,resp);

        String uri = req.getRequestURI();
        String baseWebAppName = req.getContextPath() + "/";
        uri = uri.replace(baseWebAppName,"");
        if(handleMapping.get(uri) != null){
            HandlerMapping target = handleMapping.get(uri);
            Method excutor = target.getExcutor();
            // Class[] parametors = excutor.getParameterTypes();
            Parameter[] parametors = excutor.getParameters();
            Object[] parametorValues = new Object[parametors.length];
            int i = 0;
            for(Parameter param : parametors){
                if(param.getType().isAssignableFrom(HttpServletRequest.class)){
                    parametorValues[i++] = req;
                }else if(param.getType().isAssignableFrom(HttpServletResponse.class)){
                    parametorValues[i++] = resp;
                }else{//其他类型
                    if(!param.isAnnotationPresent(RequestParam.class)){
                        continue;
                    }
                    String paramName = param.getAnnotation(RequestParam.class).value();
                            //param.getName();
                    String parametorValue = req.getParameter(paramName);

                    if(param.getType().isAssignableFrom(String.class)){
                        parametorValues[i++] = parametorValue;
                    }else{
                        if(param.getType().getSimpleName().equalsIgnoreCase("int")){
                            parametorValues[i++] = Integer.parseInt(parametorValue);
                        }else if(param.getType().getSimpleName().equalsIgnoreCase("float")){
                            parametorValues[i++] = Float.parseFloat(parametorValue);
                        }else if(param.getType().getSimpleName().equalsIgnoreCase("double")){
                            parametorValues[i++] = Double.parseDouble(parametorValue);
                        }else if(param.getType().getSimpleName().equalsIgnoreCase("Integer")){
                            parametorValues[i++] = Integer.parseInt(parametorValue);
                        }
                        //剩余其他类型
                    }
                }
            }
            String viewName  = "";
            try {
                viewName = (String)excutor.invoke(target.getBean(),parametorValues);
                viewName = VIEW_PREFIX + viewName + VIEW_SUFFIX;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            //转发
            RequestDispatcher dispatcher=req.getRequestDispatcher(viewName);
            if(dispatcher == null){
                try {
                    throw  new Exception("没找到 view！");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            dispatcher.forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    public String getInitParameter(String name) {
        return super.getInitParameter(name);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        System.out.println("开始初始化 ！！！");
        //自动扫描
        doScan("org.gq.spring.mvc");
        //自动装配
        doAutowire();
        //mvc映射
        doHandleMapping();
    }

    private void doHandleMapping() {
        for (String beanname : beanCache.keySet()) {
            Object bean = beanCache.get(beanname);

            if(bean.getClass().isAnnotationPresent(Controller.class)){
                String baseUri = bean.getClass().getAnnotationsByType(RequestMapping.class)[0].value();
                Method[] methods = bean.getClass().getDeclaredMethods();
                for(Method method : methods){
                    if(method.isAnnotationPresent(RequestMapping.class)){
                            String uri = method.getAnnotation(RequestMapping.class).value();
                            uri = baseUri + uri;
                            handleMapping.put(uri,new HandlerMapping(uri,bean,method));
                    }
                }
            }
        }
    }

    private void doAutowire() {
       List<Field> autowiredMethods = new ArrayList<>();
        try {
            for (String beanname : beanCache.keySet()) {
                Object bean = beanCache.get(beanname);

                Field[] fields = bean.getClass().getDeclaredFields();

                for(Field field : fields){
                    field.setAccessible(true);
                    if(field.isAnnotationPresent(Autowired.class)){
                        //String propName = field.getName();
                        autowiredMethods.add(field);
                    }
                    //field.setAccessible(false);
                }
                //处理方法
                for(Method m : bean.getClass().getDeclaredMethods()){
                    if(m.isAnnotationPresent(Autowired.class)&&m.getName().startsWith("set")){
                        Class propType = m.getParameterTypes()[0];
                        Object obj = beanCache.get(this.lowerFirstWord(propType.getSimpleName()));
                        if(propType.isInstance(obj)){
                            m.invoke(bean,obj);
                            //autowiredMethods.remove(m.getName());
                        }
                    }
                }
                //处理字段
                for(Field setMethod : autowiredMethods){
                    for(Method m : bean.getClass().getDeclaredMethods()){
                        if(m.getName().equalsIgnoreCase("set"+setMethod.getName())){
                            if(m.getParameterTypes().length == 1
                                    && m.getParameterTypes()[0].isAssignableFrom(setMethod.getType())){
                                //Object obj = beanCache.get(this.lowerFirstWord(setMethod));
                                for(String key : beanCache.keySet()){
                                    if(m.getParameterTypes()[0].isInstance(beanCache.get(key))){
                                        m.invoke(bean,beanCache.get(key));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void doScan(String basePackage) {
        //String basePackage  = "org.gq.spring.mvc.controller";
        String scanLocation  = this.getClass().getClassLoader().getResource("/").getPath()
                + basePackage.replace(".","/");

        File scanFile = new File(scanLocation);
        File[] fileList = scanFile.listFiles();

        for(File file : fileList) {
            if(file.isDirectory()){
                String packageName = basePackage+"."+file.getName();
                System.out.println(packageName);
                //目录
                doScan(packageName);
            }
            String classname = "";
            try {
                if (!(file.getCanonicalPath().endsWith(".class"))) {
                    continue;
                }
                classname = file.getCanonicalPath().substring(file.getCanonicalPath().lastIndexOf("\\") + 1,
                        file.getCanonicalPath().lastIndexOf("."));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Class classtype = this.getClass().getClassLoader()
                        .loadClass(basePackage + "." + classname);
                if (classtype.isAnnotationPresent(Controller.class)
                        || classtype.isAnnotationPresent(Service.class)) {//只有用controller修饰的类才会被加载
                    String beanName = lowerFirstWord(classname);
                    beanNameCache.put(beanName, basePackage + "." + classname);
                    Object obj = classtype.newInstance();
                    beanCache.put(beanName, obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //首字母小写
    private String lowerFirstWord(String word){
        char[] strCharArr = word.toCharArray();
        if(strCharArr[0] <= 90 && strCharArr[0] >= 65){
            strCharArr[0] += 32;
        }
        return new String(strCharArr);
    }
}
