package core.dependency_injection;

import core.annotations.Autowire;

import java.lang.reflect.Field;
import java.util.Arrays;

import static core.reflection.MyClasses.mapOfInstances;

public class DependencyInjectionField {
    static void autowireField(Class aClass){

        DependencyInjection.createInstanceIfClassNotExist(aClass);

        Arrays.stream(aClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowire.class))
                .forEach(field -> {
                    if (field.getType().isInterface())
                        DependencyInjectionInterface.autowireInterfaceByField(field, aClass);
                    else {
                        if (!mapOfInstances.containsKey(field.getType().getName()))
                            DependencyInjection.autowire(field.getType());

                        DependencyInjection.injectFieldInClass(field, aClass);
                    }

                });

    }
}
