package org.gqframework.core.io.support;

import org.gqframework.lang.Nullable;
import org.gqframework.util.ResourceUtils;

import java.net.URL;

/**
 * 1.判断是否是路径
 * 2.
 *
 */
public class ResourcePatternUtils {

    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    /**
     * 判断是否是合法的地址
     * @param resourceLocation
     * @return
     */
    public static boolean isUrl(@Nullable String resourceLocation){
        boolean isUrl = false;

        if(resourceLocation == null){
            return isUrl;
        }

        if(resourceLocation.startsWith(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX)){
            return true;
        }

        if(resourceLocation.startsWith(CLASSPATH_URL_PREFIX)){
            return true;
        }

        try{
            new URL(resourceLocation);
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
//        return (resourceLocation != null &&
//                (resourceLocation.startsWith(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX) ||
//                        ResourceUtils.isUrl(resourceLocation)));
    }
}
