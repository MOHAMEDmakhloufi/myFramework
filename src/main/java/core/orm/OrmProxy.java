package core.orm;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class OrmProxy implements InvocationHandler {

    private Object target;

    public OrmProxy(Object target){
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object result = method.invoke(target, args);

        return result;
    }

    public static Object createProxy(Object target, Class<?> realClass){
        return Proxy.newProxyInstance(realClass.getClassLoader() ,
                new Class[] { realClass },
                new OrmProxy(target));
    }
}
