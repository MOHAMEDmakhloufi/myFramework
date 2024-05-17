package core.reflection;

import core.annotations.Autowire;
import core.ioc.MyFrameworkApplication;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyClasses {

   

    public static Class mainClass;
    public static List<Class> classes;
    public static Map<String, Object> mapOfInstances;
    
    private static String getCleanParent(Path path){
        String parent = path.getParent().toString();
        return parent.substring(
                parent.lastIndexOf(mainClass.getPackageName().replace(".", "\\"))
        ).replace("\\", ".");
    }

    private static String getClassName(Path path){

        return  path.getFileName().toString().substring(0,
                path.getFileName().toString().lastIndexOf(".")
        );
    }

    private static String generateClassname(Path path){
        return getCleanParent(path)+"."+getClassName(path);
    }

    private static List<Path> extractPaths(Class className) throws Exception {
        Path path = new File(
                "src/main/java/"+className.getPackageName().replace(".", "/")
        ).toPath();

        return Files.walk(path)
                .filter(p -> p.toFile().isFile())
                .filter(p -> p.getFileName().toString().contains(".java"))
                .collect(Collectors.toList());
    }

    public static List<Class> extractClasses(Class nameOfClass) throws Exception {
        return extractPaths(nameOfClass).stream()
                .map(MyClasses::generateClassname)
                .map(className -> {
                    try {
                        return Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }

    public static List<Class> getClassesHasAnnotation(Class annotation){

        return classes.stream()
                .filter(aClass -> aClass.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }
    public static List<Class> getClassesImplementInterface(Class anInterface){
        return classes.stream()
                .filter(aClass1 -> Arrays.asList(aClass1.getInterfaces()).contains(anInterface)).toList();
    }
    public static boolean isClassHasFieldAnnotated(Class aClass){
        return Arrays.stream(aClass.getDeclaredFields())
                .anyMatch(field -> field.isAnnotationPresent(Autowire.class));
    }
    public static boolean isClassHasConstructorAnnotated(Class aClass){
        return Arrays.stream(aClass.getDeclaredConstructors())
                .anyMatch(c -> c.isAnnotationPresent(Autowire.class));
    }
    public static boolean isClassHasSetAnnotated(Class aClass){
        return Arrays.stream(aClass.getDeclaredMethods())
                .anyMatch(m -> m.isAnnotationPresent(Autowire.class));
    }
}
