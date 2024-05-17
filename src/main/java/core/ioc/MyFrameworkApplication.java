package core.ioc;

import core.annotations.Component;
import core.annotations.FakeController;
import core.dependency_injection.DependencyInjection;
import core.fake_controller.Controllers;
import core.orm.MyOrm;
import core.reflection.MyClasses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyFrameworkApplication {



    public static void run (Class mainClass) throws Exception {
        //initialize the static attributes
        MyClasses.mapOfInstances = new HashMap<>();

        MyClasses.mainClass = mainClass;

        //scan packages and get all classes
        MyClasses.classes = MyClasses.extractClasses(mainClass);

        MyOrm.createTables();
        MyOrm.createRepositories();
        // find classes have component annotation and autowire them
        List<Class> classesHasComponent = MyClasses.getClassesHasAnnotation(Component.class);

        System.out.println("\nKEY = CLASS , VALUE = INSTANCE\n");
        classesHasComponent.forEach(DependencyInjection::autowire);
        MyClasses.mapOfInstances.forEach((k, v)-> System.out.println(String.format("key %s value %s", k, v)));
        System.out.println("\nSTART LOGGING YOUR APPLICATION\n");
        // find classes have component annotation and invoke their methods
        List<Class> classesHasFakeController = MyClasses.getClassesHasAnnotation(FakeController.class);

        classesHasFakeController.forEach(aClass -> Controllers.invokeControllersMethods(aClass));
    }
}
