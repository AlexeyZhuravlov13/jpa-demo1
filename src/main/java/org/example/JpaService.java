package org.example;

import org.example.annotations.Column;
import org.example.annotations.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JpaService {

    private static final String URL = "jdbc:mysql://localhost:3306/users_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public List<Object> findAll(Class<?> aClass) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Entity annotation = aClass.getDeclaredAnnotation(Entity.class);
        List<Object> resultList = new ArrayList<>();
        if (annotation == null) {
            return resultList;
        }
        String tableName = annotation.tableName();
        String sql = String.format("select * from %s", tableName);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            resultList = getEntitiesFromResultSet(resultSet, aClass);
        } catch (SQLException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    private List<Object> getEntitiesFromResultSet(ResultSet resultSet, Class<?> aClass) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Object> entities = new ArrayList<>();
        Field[] declaredFields = aClass.getDeclaredFields();
        while (resultSet.next()) {
            Constructor<?> declaredConstructor = aClass.getDeclaredConstructor();
            Object instance = declaredConstructor.newInstance();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    String columnName = column.columnName();
                    Object value = getColumnValue(resultSet, field.getType(), columnName);
                    field.set(instance, value);
                }
            }
            entities.add(instance);
        }
        return entities;
    }

    private Object getColumnValue(ResultSet resultSet, Class<?> type, String columnName) throws SQLException {
        if (type == int.class || type == Integer.class) {
            return resultSet.getInt(columnName);
        } else if (type == String.class) {
            return resultSet.getString(columnName);
        } else if (type == long.class || type == Long.class) {
            return resultSet.getLong(columnName);
        } else if (type == double.class || type == Double.class) {
            return resultSet.getDouble(columnName);
        } else if (type == boolean.class || type == Boolean.class) {
            return resultSet.getBoolean(columnName);
        } else if (type == Date.class) {
            return resultSet.getDate(columnName);
        } else {
            return null;
        }
    }
}
