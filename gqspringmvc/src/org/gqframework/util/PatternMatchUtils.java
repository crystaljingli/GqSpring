package org.gqframework.util;

/**
 * 根据通配符进行匹配，xxx,yyy,xxx*yyy,*yyy,**yyy,xxx*yyy*ccc等
 */
public class PatternMatchUtils {

    public static boolean simpleMatch(String pattern,String str){

        if(pattern == null || str == null){
            return false;
        }

        int firstindex = pattern.indexOf("*");

        if(firstindex == 0){
            int nextindex = pattern.indexOf("*",firstindex + 1);
            if(nextindex == -1){
                return str.endsWith(pattern.substring(firstindex + 1));
            }
            String part = pattern.substring(1,nextindex);

            if("".equals(part)){
                return simpleMatch(pattern.substring(nextindex),str);
            }

            int partIndex = str.indexOf(part);

            while(partIndex != -1){
                if(simpleMatch(pattern.substring(nextindex),str.substring(partIndex + part.length()))){
                    return true;
                }
                partIndex = str.indexOf(part,partIndex + 1);
            }
            return false;
        }
        return (str.length() >= firstindex &&
                        pattern.substring(0,firstindex).equals(str.substring(0,firstindex))&&
                        simpleMatch(pattern.substring(firstindex),str.substring(firstindex)));
    }

    public static boolean simpleMatch(String[] patterns,String str){
        if(patterns != null){
            for(String pattern : patterns){
                if(simpleMatch(pattern,str)){
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String args[]){
        String pattern = "aaa*bbb*ccc";
        String str = "aaacccbbbdddbbbdddccc";
        System.out.println(simpleMatch(pattern,str));
    }
}

