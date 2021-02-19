package com.fuhu.demo.h2.mapper;

import com.fuhu.demo.h2.entity.Cat;

import java.util.List;

public interface CatMapper {
    List<Cat> selectAll();
}
