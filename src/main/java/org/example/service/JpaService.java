package org.example.service;

import org.example.annotation.Column;
import org.example.annotation.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JpaService<T> {

    private static final String URL = "jdbc:mysql://localhost:3306/users_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public List<T> findAll(Class<T> aClass) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Entity annotation = aClass.getDeclaredAnnotation(Entity.class);
        List<T> resultList = new ArrayList<>();
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

    private List<T> getEntitiesFromResultSet(ResultSet resultSet, Class<T> aClass) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<T> entities = new ArrayList<>();
        Field[] declaredFields = aClass.getDeclaredFields();
        while (resultSet.next()) {
            Constructor<T> declaredConstructor = aClass.getDeclaredConstructor();
            T instance = declaredConstructor.newInstance();
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
        return switch (type.getSimpleName()) {
            case "int", "Integer" -> resultSet.getInt(columnName);
            case "String" -> resultSet.getString(columnName);
            case "long", "Long" -> resultSet.getLong(columnName);
            case "double", "Double" -> resultSet.getDouble(columnName);
            case "boolean", "Boolean" -> resultSet.getBoolean(columnName);
            case "Date" -> resultSet.getDate(columnName);
            default -> null;
        };
    }
}
