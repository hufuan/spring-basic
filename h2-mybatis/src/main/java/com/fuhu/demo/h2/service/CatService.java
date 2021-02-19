package com.fuhu.demo.h2.service;

import com.fuhu.demo.h2.entity.Cat;

import java.util.List;

public interface CatService {

    /**
     * 喵叫
     * @return
     */
    String meow();

    List<Cat> list();

}