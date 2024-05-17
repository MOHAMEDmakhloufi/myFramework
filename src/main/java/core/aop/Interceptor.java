package core.aop;


import core.annotations.After;
import core.annotations.Aspect;
import core.annotations.Before;
import core.reflection.MyClasses;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class Interceptor implements InvocationHandler {

    private Object target;

    public Interceptor(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        invokeMethodsOfAspect(Before.class, method);

        Object result = method.invoke(target, args);

        invokeMethodsOfAspect(After.class, method);

        return result;
    }

    public static Object createProxy(Object target){

        return Proxy.newProxyInstance(target.getClass().getClassLoader() ,
                target.getClass().getInterfaces(),
                new Interceptor(target));
    }

    private void invokeMethodsOfAspect(Class cutPoint, Method method){

        MyClasses.getClassesHasAnnotation(Aspect.class)
                .stream().forEach(aClass ->

                    Arrays.stream(aClass.getDeclaredMethods())
                            .filter(meth -> meth.isAnnotationPresent(cutPoint))
                            .filter(meth -> {
                                Annotation annotation = meth.getAnnotation(cutPoint);
                                try {

                                    return ((String) annotation.annotationType().getMethod("value").invoke(annotation))
                                            .contains(method.getDeclaringClass().getName()+"."+method.getName());
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
