package org.example.entity;

import org.example.annotations.Column;
import org.example.annotations.Entity;

@Entity(tableName = "users")
public class User {

    @Column(columnName = "id")
    private Integer id;

    @Column(columnName = "name")
    private String name;

    @Column(columnName = "age")
    private int age;
}
