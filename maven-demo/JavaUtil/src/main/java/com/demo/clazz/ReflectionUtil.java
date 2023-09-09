package com.demo.clazz;

import org.apache.commons.collections4.CollectionUtils;
import org.reflections.Reflections;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author XAD on  2023/9/9 21:55
 */
public class ReflectionUtil {


    public static <T> List<Class<? extends T>> findClassesInPackage(String packageName, Class<T> tClass) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends T>> allClasses = reflections.getSubTypesOf(tClass);
        if (CollectionUtils.isNotEmpty(allClasses)) {
            return allClasses.stream()
                    .filter(lt -> !tClass.getTypeName().equals(lt.getTypeName()))
                    .collect(Collectors.toList());
        }
        return null;
    }


    public static List<Class<?>> findClassesInPackage(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);
        if (CollectionUtils.isNotEmpty(allClasses)) {
            return allClasses.stream()
                    .filter(lt -> !Object.class.getTypeName().equals(lt.getTypeName()))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
