package core.fake_controller;

import core.reflection.MyClasses;

import java.util.Arrays;

public class Controllers {
    public static void invokeControllersMethods(Class aClass){
        Arrays.stream(aClass.getDeclaredMethods())
                .filter(method -> !method.getName().contains("set"))
                .filter(method -> method.getParameterCount()==0)
                .forEach(method -> {
                    Object instance = MyClasses.mapOfInstances.get(aClass.getName());

                    try {
                        method.invoke(instance);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
