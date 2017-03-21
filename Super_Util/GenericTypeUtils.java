package com.chapter7.test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by sheldonshen on 2017/3/21.
 */
public class GenericTypeUtils {
     //获的一个泛型类的实际泛型类型
     public static <T> Class<T> getGenericClassType(Class clz){
         Type type = clz.getGenericSuperclass();
         if(type instanceof ParameterizedType){
             ParameterizedType pt = (ParameterizedType) type;
             Type[] types = pt.getActualTypeArguments();
             if(types.length > 0 && types[0] instanceof Class){
                 //若有多个泛型参数,依据位置索引返回
                 return (Class) types[0];
             }
         }
         return (Class) Object.class;
     }
}
