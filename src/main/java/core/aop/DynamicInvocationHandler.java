package core.aop;

import core.annotations.After;
import core.annotations.Aspect;
import core.annotations.Before;
import core.reflection.MyClasses;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class DynamicInvocationHandler implements MethodHandler {
    public Object getTarget() {
        return target;
    }

    private Object target;

    public DynamicInvocationHandler(Object target){
        this.target = target;
    }

    @Override
    public Object invoke(Object self, Method m, Method proceed, Object[] args) throws Throwable {

        MyAop.invokeMethodsOfAspect(Before.class, m);

        Object result = m.invoke(target, args);

        MyAop.invokeMethodsOfAspect(After.class, m);

        return result;
    }

    public static Object createProxy(Object target, Class<?>[] parameterTypes, List<Object> parameterInstances) throws Exception {
        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(target.getClass());

        Class<?> proxyClass = factory.createClass();
        Object proxyInstance;

        if (parameterTypes == null) {
            proxyInstance = proxyClass.getConstructor().newInstance();
        } else {
            proxyInstance =  proxyClass.getConstructor(parameterTypes)
                    .newInstance(parameterInstances.toArray());;

        }

        ((javassist.util.proxy.ProxyObject) proxyInstance).setHandler(new DynamicInvocationHandler(target));

        return proxyInstance;
    }


}

