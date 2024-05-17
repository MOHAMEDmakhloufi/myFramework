package core.orm;
import core.annotations.*;

import core.aop.DynamicInvocationHandler;
import core.reflection.MyClasses;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import static core.reflection.MyClasses.mapOfInstances;

public class MyOrm {
    public static void createTables(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        MyClasses.getClassesHasAnnotation(Entity.class).stream()
                .forEach(aClass -> {
                    long countFieldHasAnnId = Arrays.stream(aClass.getDeclaredFields())
                            .filter(field -> field.isAnnotationPresent(Id.class)).count();
                    if (countFieldHasAnnId !=1)
                        throw new RuntimeException(String.format("The entity %s need one Id field ", aClass.getSimpleName()));

                    StringBuilder sqlCreateTableQueryBuilder= new StringBuilder();
                    String tableName= aClass.getSimpleName();
                    if (aClass.isAnnotationPresent(Table.class) && !((Table)aClass.getAnnotation(Table.class)).value().isEmpty())
                        tableName = ((Table)aClass.getAnnotation(Table.class)).value();
                    sqlCreateTableQueryBuilder.append("CREATE TABLE IF NOT EXISTS "+tableName+"(\n");

                    Arrays.stream(aClass.getDeclaredFields())
                            .forEach(field -> {
                                if (field.isAnnotationPresent(Id.class)){
                                    if (field.isAnnotationPresent(GeneratedValue.class) &&
                                            field.getAnnotation(GeneratedValue.class).strategy().equals(GenerationType.AUTO))
                                        sqlCreateTableQueryBuilder.append("\t"+field.getName()+" "+UtilisSQL_JAVA.getRelationalType(field)+" AUTO_INCREMENT PRIMARY KEY NOT NULL,\n");
                                    else{
                                        if (field.isAnnotationPresent(GeneratedValue.class) &&
                                                field.getAnnotation(GeneratedValue.class).strategy().equals(GenerationType.SEQUENCE))
                                            createSequence(field);
                                        sqlCreateTableQueryBuilder.append("\t"+field.getName()+" "+UtilisSQL_JAVA.getRelationalType(field)+" PRIMARY KEY NOT NULL,\n");

                                    }
                                }
                                else
                                    sqlCreateTableQueryBuilder.append("\t"+field.getName()+" "+UtilisSQL_JAVA.getRelationalType(field)+",\n");
                            });
                    sqlCreateTableQueryBuilder.delete(sqlCreateTableQueryBuilder.length()-2, sqlCreateTableQueryBuilder.length()-1);
                    sqlCreateTableQueryBuilder.append("\n);");

                    jdbcTemplate.update(sqlCreateTableQueryBuilder.toString(), new String[0]);
                });
    }

    public static void createRepositories(){
        MyClasses.getClassesHasAnnotation(Repository.class).stream()
                .forEach(aClass -> {
                    Type genericInterfaces = aClass.getGenericInterfaces()[0];
                    if (genericInterfaces instanceof ParameterizedType == false)
                        throw new RuntimeException(aClass.getSimpleName() +" is not ParameterizedType");
                    ParameterizedType parameterizedType = (ParameterizedType) genericInterfaces;
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    Class<?> t = null;
                    Class<?> i = null;
                    try {
                        t = Class.forName(actualTypeArguments[0].getTypeName());
                        i = Class.forName(actualTypeArguments[1].getTypeName());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    Object newInstance = OrmProxy.createProxy(new CrudRepositoryImpl<>(t, i), aClass);
                    mapOfInstances.put(aClass.getName(), newInstance);
                });
    }
    private static void createSequence(Field field) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        String sqlCreateSequence= String.format("CREATE SEQUENCE %s\n" +
                "    START WITH 1\n" +
                "    INCREMENT BY 1\n" +
                "    NOMAXVALUE;", field.getAnnotation(SequenceName.class).value());
        jdbcTemplate.update(sqlCreateSequence, new String[0]);
    }
}
