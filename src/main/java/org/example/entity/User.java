package org.example.entity;

import org.example.annotation.Column;
import org.example.annotation.Entity;

@Entity(tableName = "users")
public class User {

    @Column(columnName = "id")
    private Integer id;

    @Column(columnName = "name")
    private String name;

    @Column(columnName = "age")
    private int age;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
