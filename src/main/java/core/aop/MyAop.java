package core.aop;

import core.annotations.Aspect;
import core.reflection.MyClasses;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MyAop {

    public static void invokeMethodsOfAspect(Class cutPoint, Method method){

        MyClasses.getClassesHasAnnotation(Aspect.class)
                .stream().forEach(aClass ->

                        Arrays.stream(aClass.getDeclaredMethods())
                                .filter(meth -> meth.isAnnotationPresent(cutPoint))
                                .filter(meth -> {
                                    Annotation annotation = meth.getAnnotation(cutPoint);
                                    try {
                                        String value = ((String) annotation.annotationType().getMethod("value").invoke(annotation));
                                        boolean testInterfaces = false;
                                        if (!method.getDeclaringClass().isInterface())
                                            for (Class i : method.getDeclaringClass().getInterfaces())
                                                if (value.contains(i.getName()+"."+method.getName())){
                                                    testInterfaces= true;
                                                    break;
                                                }
                                        return value.contains(method.getDeclaringClass().getName()+"."+method.getName()) || testInterfaces;
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                                .forEach(meth -> {

                                    try {
                                        meth.invoke(aClass.getConstructor().newInstance());
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                })

                );

    }
}
