package org.example;

import org.example.entity.User;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
            JpaService jpaService = new JpaService();
        List<Object> all = jpaService.findAll(User.class);
        System.out.println(Arrays.toString(all.toArray()));
    }
    }