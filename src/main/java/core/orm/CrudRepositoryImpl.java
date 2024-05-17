package core.orm;


import core.annotations.Id;
import core.annotations.Table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CrudRepositoryImpl<T, ID> implements CrudRepository<T, ID> {
    private Class<T> entityType;
    private Class<ID> idType;

    public CrudRepositoryImpl(Class<T> entityType, Class<ID> idType) {
        this.entityType = entityType;
        this.idType = idType;
    }

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    @Override
    public T save(T entity) {
        StringBuilder sql= new StringBuilder();
        StringBuilder values= new StringBuilder();
        //INSERT INTO `persons`(`id`, `name`, `email`) VALUES ('[value-1]','[value-2]','[value-3]')
        String tableName = getTableName();

        sql.append("INSERT INTO ").append(tableName+"(");
        values.append("(");
        Arrays.stream(entity.getClass().getDeclaredFields())
                .forEach(field -> {
                    field.setAccessible(true);
                    sql.append(field.getName()+",");
                    try {
                        values.append("'"+field.get(entity)+"',");
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
        sql.deleteCharAt(sql.length()-1);
        values.deleteCharAt(values.length()-1);
        sql.append(") VALUES ");
        values.append(")");
        sql.append(values.toString());

        Object result = jdbcTemplate.update(sql.toString(), new String[0]);

        Arrays.stream(entity.getClass().getDeclaredFields()).filter(field -> field.isAnnotationPresent(Id.class))
                .forEach(field -> {
                    try {

                        field.setAccessible(true);
                        field.set(entity, result);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
        return entity;
    }

    @Override
    public Optional<T> findById(ID id) {
        //SELECT `id`, `name`, `email` FROM `persons` WHERE 1
        StringBuilder sql= new StringBuilder();
        StringBuilder idName= getFieldId();

        sql.append("SELECT *  FROM ");
        String tableName = getTableName();
        sql.append(tableName);
        sql.append(" WHERE ").append(idName).append(" = ").append(id);
        ResultSet resultSet = jdbcTemplate.query(sql.toString(), new String[0]);

        try {
            T instance = null;
            if (resultSet.next()){
                instance = createInstanceFromResultSet(resultSet);
                resultSet.close();
                jdbcTemplate.endTransaction();
                jdbcTemplate.closeConnection();
                return Optional.of(instance);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        //SELECT `id`, `name`, `email` FROM `persons`
        StringBuilder sql= new StringBuilder();
        StringBuilder idName= new StringBuilder();

        sql.append("SELECT * FROM ");
        String tableName = getTableName();
        sql.append(tableName);

        ResultSet resultSet = jdbcTemplate.query(sql.toString(), new String[0]);

        try {
            List<T> instances = ArrayList.class.getDeclaredConstructor().newInstance();
            while (resultSet.next()){
                instances.add(createInstanceFromResultSet(resultSet));


            }
            resultSet.close();
            jdbcTemplate.endTransaction();
            jdbcTemplate.closeConnection();
            return instances;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean existsById(ID id) {
        //SELECT `id`, `name`, `email` FROM `persons` WHERE 1
        StringBuilder sql= new StringBuilder();
        StringBuilder idName= getFieldId();

        sql.append("SELECT *  FROM ");
        String tableName = getTableName();
        sql.append(tableName);
        sql.append(" WHERE ").append(idName).append(" = ").append(id);
        ResultSet resultSet = jdbcTemplate.query(sql.toString(), new String[0]);

        try {

            if (resultSet.next()){
                resultSet.close();
                jdbcTemplate.endTransaction();
                jdbcTemplate.closeConnection();
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void deleteById(ID id) {
        //DELETE FROM `persons` WHERE id= id
        StringBuilder sql= new StringBuilder();
        StringBuilder idName= getFieldId();

        sql.append("DELETE FROM ");

        String tableName = getTableName();
        sql.append(tableName);
        sql.append(" WHERE ").append(idName).append(" = ").append(id);
        jdbcTemplate.update(sql.toString(), new String[0]);


    }

    @Override
    public long count() {
        //SELECT COUNT(*) FROM `persons`
        StringBuilder sql= new StringBuilder();
        int count = 0;
        sql.append("SELECT COUNT(*)");
        
        sql.append(" FROM ");
        String tableName = getTableName();
        sql.append(tableName);

        ResultSet resultSet = jdbcTemplate.query(sql.toString(), new String[0]);

        try {

            if (resultSet.next()){
                count = resultSet.getInt(1);

            }
            resultSet.close();
            jdbcTemplate.endTransaction();
            jdbcTemplate.closeConnection();

        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }

    private T createInstanceFromResultSet(ResultSet resultSet) throws SQLException, Exception {
        T instance = this.entityType.getConstructor().newInstance();

        Arrays.stream(this.entityType.getDeclaredFields())
                .forEach(field -> {

                    try {
                        field.setAccessible(true);
                        int sqlType = resultSet.getMetaData().getColumnType(resultSet.findColumn(field.getName()));

                        Object value = resultSet.getObject(field.getName());
                        Object javaValue = UtilisSQL_JAVA.castSQLValue(value, sqlType);
                        field.set(instance, javaValue);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                });
        return instance;


    }

    private String getTableName(){
        String tableName = entityType.getSimpleName();
        if (entityType.getClass().isAnnotationPresent(Table.class)){
            String value = entityType.getClass().getAnnotation(Table.class).value();
            tableName = value.isEmpty()? tableName: value;
        }
        return tableName;
    }

    private StringBuilder getFieldId(){
        StringBuilder idName= new StringBuilder();
        Arrays.stream(this.entityType.getDeclaredFields())
                .forEach(field -> {
                    if (field.isAnnotationPresent(Id.class))
                        idName.append( field.getName() );
                    field.setAccessible(true);

                });
        return idName;
    }
}
