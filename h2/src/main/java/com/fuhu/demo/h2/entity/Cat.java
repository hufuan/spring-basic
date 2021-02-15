package com.fuhu.demo.h2.entity;

import lombok.Data;

@Data
public class Cat {
    private Long id;
    private String name;
    private Integer age;
    private String color;
    private Double score;
}