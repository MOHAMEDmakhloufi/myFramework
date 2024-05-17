package core.dependency_injection;

import core.annotations.Component;
import core.annotations.Qualifier;
import core.aop.DynamicInvocationHandler;
import core.reflection.MyClasses;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import static core.reflection.MyClasses.mapOfInstances;

public class DependencyInjectionInterface {

    static void autowireInterfaceByField(Field field, Class parent){
        Class implementation = field.getType();
        if (!mapOfInstances.containsKey(implementation.getName())){
            List<Class> classesImplementInterface = MyClasses.getClassesImplementInterface(field.getType());
            if (field.isAnnotationPresent(Qualifier.class))
                classesImplementInterface = classesImplementInterface.stream()
                        .filter(aClass -> {
                            String qualifierValue = field.getAnnotation(Qualifier.class).value();
                            String componentValue = ((Component) aClass.getAnnotation(Component.class)).value();
                            return qualifierValue.equals(componentValue);
                        }).toList();

            if (classesImplementInterface.isEmpty())
                throw new RuntimeException(String.format("cannot found component implement an interface %s", field.getName()));

            implementation = classesImplementInterface.get(0);

            if (!mapOfInstances.containsKey(implementation.getName()))
                DependencyInjection.autowire(implementation);
        }

        ProxyObject proxyObject =(ProxyObject) mapOfInstances.get(parent.getName());


        MethodHandler handler = proxyObject.getHandler();


        Object targetObject = ((DynamicInvocationHandler) handler).getTarget();
        try {
            field.setAccessible(true);

            field.set(targetObject, mapOfInstances.get(implementation.getName()));


        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }


    static void autowireInterfaceBySet(Method method, Class parent){
        Class paramType = method.getParameterTypes()[0];
        Class implementation = paramType;
        if (!mapOfInstances.containsKey(implementation.getName())){
            List<Class> classesImplementInterface = MyClasses.getClassesImplementInterface(paramType);
            if (method.isAnnotationPresent(Qualifier.class))
                classesImplementInterface = classesImplementInterface.stream()
                        .filter(aClass -> {
                            String qualifierValue = method.getAnnotation(Qualifier.class).value();
                            String componentValue = ((Component) aClass.getAnnotation(Component.class)).value();
                            return qualifierValue.equals(componentValue);
                        }).toList();

            if (classesImplementInterface.isEmpty())
                throw new RuntimeException(String.format("cannot found component implement an interface %s", paramType.getName()));

            implementation = classesImplementInterface.get(0);

            if (!mapOfInstances.containsKey(implementation.getName()))
                DependencyInjection.autowire(implementation);
        }

        Object instance = mapOfInstances.get(parent.getName());
        try {

            method.invoke(instance, mapOfInstances.get(implementation.getName()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static String autowireInterfaceByConstructor(Parameter parameter){
        Class paramType = parameter.getType();
        Class implementation = paramType;
        if (!mapOfInstances.containsKey(implementation.getName())){
            List<Class> classesImplementInterface = MyClasses.getClassesImplementInterface(paramType);
            if (parameter.isAnnotationPresent(Qualifier.class))
                classesImplementInterface = classesImplementInterface.stream()
                        .filter(aClass -> {
                            String qualifierValue = parameter.getAnnotation(Qualifier.class).value();
                            String componentValue = ((Component) aClass.getAnnotation(Component.class)).value();
                            return qualifierValue.equals(componentValue);
                        }).toList();

            if (classesImplementInterface.isEmpty())
                throw new RuntimeException(String.format("cannot found component implement an interface %s", paramType.getName()));

            implementation = classesImplementInterface.get(0);

            if (!mapOfInstances.containsKey(implementation.getName()))
                DependencyInjection.autowire(implementation);
        }

        return implementation.getName();
    }
}
