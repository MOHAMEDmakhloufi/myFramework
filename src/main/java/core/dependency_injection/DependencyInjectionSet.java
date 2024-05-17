package core.dependency_injection;

import core.annotations.Autowire;

import java.lang.reflect.Method;
import java.util.Arrays;

import static core.reflection.MyClasses.mapOfInstances;

public class DependencyInjectionSet {

    static void autowireSet(Class aClass){

        DependencyInjection.createInstanceIfClassNotExist(aClass);

        Arrays.stream(aClass.getDeclaredMethods())
                .filter(method -> method.getName().contains("set") && method.getParameterTypes().length==1)
                .filter(method -> method.isAnnotationPresent(Autowire.class))
                .forEach(method -> {
                    Class paramType = method.getParameterTypes()[0];

                    if (paramType.isInterface())
                        DependencyInjectionInterface.autowireInterfaceBySet(method, aClass);
                    else{
                        if (!mapOfInstances.containsKey(paramType.getName()))
                            DependencyInjection.autowire(paramType);

                        DependencyInjection.injectParamaterInClass(paramType, method, aClass);
                    }

                });
    }
}
