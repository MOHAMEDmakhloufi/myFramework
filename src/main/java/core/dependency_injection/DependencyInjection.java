package core.dependency_injection;

import core.aop.DynamicInvocationHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static core.reflection.MyClasses.*;

public class DependencyInjection {
    public static void autowire(Class aClass){

        if (isClassHasConstructorAnnotated(aClass)){
            DependencyInjectionConstructor.autowireConstructor(aClass);

        }if (isClassHasFieldAnnotated(aClass)){
            DependencyInjectionField.autowireField(aClass);
        }if (isClassHasSetAnnotated(aClass)){
            DependencyInjectionSet.autowireSet(aClass);
        }if (!mapOfInstances.containsKey(aClass.getName()) &&
                !isClassHasConstructorAnnotated(aClass) &&
                !isClassHasFieldAnnotated(aClass) &&
                !isClassHasSetAnnotated(aClass)
        ){
            try {

                Object newInstance = DynamicInvocationHandler.createProxy(aClass.getConstructor().newInstance(), null, null);
                mapOfInstances.put(aClass.getName(), newInstance);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static void createInstanceIfClassNotExist(Class aClass) {
        if (!mapOfInstances.containsKey(aClass.getName())){
            try {
                Object newInstance = DynamicInvocationHandler.createProxy(aClass.getConstructor().newInstance(), null , null);
                mapOfInstances.put(aClass.getName(), newInstance);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void injectFieldInClass(Field field, Class aClass) {
        Object instance = mapOfInstances.get(aClass.getName());
        try {
            field.setAccessible(true);

            field.set(instance, mapOfInstances.get(field.getType().getName()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void injectParamaterInClass(Class paramType, Method method, Class aClass) {
        Object instance = mapOfInstances.get(aClass.getName());

        try {
            method.invoke(instance, mapOfInstances.get(paramType.getName()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
