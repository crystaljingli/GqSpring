package org.gqframework.util;

import javax.servlet.ServletContext;
import java.io.FileNotFoundException;

/**
 * @Description
 * @Author crystal
 * @CreatedDate 2018年06月03日 星期日 10时16分.
 */
public class WebUtils {

    public static String getRealPath(ServletContext servletContext, String path) {
        Assert.notNull(servletContext, "ServletContext must not be null");
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        String realPath = servletContext.getRealPath(path);
        if (realPath == null) {
            try {
                throw new Exception("ServletContext resource [" + path + "] cannot be resolved to absolute file path - web application archive not expanded?");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return realPath;
        }
        return null;
    }

}
