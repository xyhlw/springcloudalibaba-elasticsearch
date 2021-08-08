package com.xy.cloud.search.utli;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 中文转拼音工具类
 * @since 2020-8-6
 */
public class PinyinUtil {

    public static  List<String>  getFormatName(String name,Integer result){
     //统一转为小写
     String queryName = name.toLowerCase();
     List<String> list = new ArrayList<>();
     list.add(queryName);
        //判断当前词是否是专业词语不分词
     if(result == 0){
         if(queryName.contains("-")||queryName.contains("_")||queryName.contains(".")||queryName.contains(",")||queryName.contains("%")||queryName.contains(" ")){
             String[] arrayName = queryName.split("[\\+\\-=\\_\\.\\,\\%]");
             for(String names : arrayName){
                 list.add(names);
             }
             return  list;
         }
     }
        return list;
    }

}
