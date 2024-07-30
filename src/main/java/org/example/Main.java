package org.example;

import org.example.entity.User;
import org.example.service.JpaService;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        JpaService<User> jpaService = new JpaService<>();
        List<User> all = jpaService.findAll(User.class);
        System.out.println(Arrays.toString(all.toArray()));
    }
}