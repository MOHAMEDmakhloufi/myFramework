package core.orm;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Types;

public class UtilisSQL_JAVA {
    public static String  getRelationalType(Field field){

        switch (field.getType().getSimpleName()) {
            case "String":
                return "varchar(255)";
            case "int":
            case "Integer":
                return "int";
            case "long":
            case "Long":
                return "bigint";
            case "boolean":
            case "Boolean":
                return "boolean";
            case "float":
            case "Float":
                return "float";
            case "double":
            case "Double":
                return "double";
            // Add more cases for other Java types as needed
            default:
                // If the field type is not mapped, return a generic type
                return "varchar(255)";
        }
    }
    public static Object castSQLValue(Object value, int sqlType) throws SQLException {
        switch (sqlType) {
            case Types.VARCHAR:
                return value != null ? value.toString() : null;
            case Types.INTEGER:
                if (value instanceof Integer) {
                    return value;
                } else if (value instanceof Long) {
                    return ((Long) value).intValue();
                } else if (value instanceof BigInteger) {
                    return ((BigInteger) value).intValueExact(); // Convert BigInteger to int
                } else if (value instanceof Number) {
                    return ((Number) value).intValue();
                } else {
                    return null;
                }
            case Types.BIGINT:
                if (value instanceof Long) {
                    return value;
                } else if (value instanceof Integer) {
                    return ((Integer) value).longValue();
                } else if (value instanceof BigInteger) {
                    return ((BigInteger) value).longValueExact(); // Convert BigInteger to long
                } else if (value instanceof Number) {
                    return ((Number) value).longValue();
                } else {
                    return null;
                }
            case Types.BOOLEAN:
                return (value instanceof Boolean) ? value : null;
            case Types.FLOAT:
                if (value instanceof Float) {
                    return value;
                } else if (value instanceof Double) {
                    return ((Double) value).floatValue();
                } else if (value instanceof Number) {
                    return ((Number) value).floatValue();
                } else {
                    return null;
                }
            case Types.DOUBLE:
                if (value instanceof Double) {
                    return value;
                } else if (value instanceof Float) {
                    return ((Float) value).doubleValue();
                } else if (value instanceof Number) {
                    return ((Number) value).doubleValue();
                } else {
                    return null;
                }
                // Add more cases for other SQL types as needed
            default:
                return value;
        }
    }
}
