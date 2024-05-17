package core.dependency_injection;

import core.annotations.Autowire;
import core.aop.DynamicInvocationHandler;
import core.aop.Interceptor;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static core.reflection.MyClasses.mapOfInstances;

public class DependencyInjectionConstructor {

    static void autowireConstructor(Class aClass){
        Arrays.stream(aClass.getDeclaredConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(Autowire.class))
                .forEach(constructor -> {
                    List<Parameter> paramTypes = Arrays.stream(constructor.getParameters())
                            .collect(Collectors.toList());
                    List<String> paramNames = paramTypes.stream()
                            .map(Parameter::getType)
                            .map(Class::getName)
                            .collect(Collectors.toList());
                    paramTypes.stream()
                            .filter(p -> !mapOfInstances.containsKey(p.getName()) && !p.getType().isInterface())
                            .forEach(p -> {
                                DependencyInjection.autowire(p.getType());
                            });
                    paramTypes.stream()
                            .filter(p -> p.getType().isInterface())
                            .forEach(p -> {
                                String fullParamName = DependencyInjectionInterface
                                        .autowireInterfaceByConstructor(p);
                                int index = paramNames.indexOf(p.getType().getName());
                                paramNames.remove(index);
                                paramNames.add(index, fullParamName);
                            });
                    List<Object> instances = paramNames.stream()
                            .map(p -> mapOfInstances.get(p)).collect(Collectors.toList());
                    try {
                        Object newInstance =  aClass.getConstructor(constructor.getParameterTypes())
                                .newInstance(instances.toArray());
                        newInstance = DynamicInvocationHandler.createProxy(newInstance, constructor.getParameterTypes(), instances);
                        mapOfInstances.put(aClass.getName(), newInstance);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
