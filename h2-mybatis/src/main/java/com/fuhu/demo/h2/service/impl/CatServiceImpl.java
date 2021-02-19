package com.fuhu.demo.h2.service.impl;

import com.fuhu.demo.h2.entity.Cat;
import com.fuhu.demo.h2.mapper.CatMapper;
import com.fuhu.demo.h2.service.CatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatServiceImpl implements CatService {

    @Autowired
    private CatMapper catMapper;

    @Override
    public String meow() {
        return "瞄";
    }

    @Override
    public List<Cat> list() {
        return catMapper.selectAll();
    }
}