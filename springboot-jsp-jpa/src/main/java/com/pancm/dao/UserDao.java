package com.pancm.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancm.pojo.User;

/**
 * 
* Title: UserDao
* Description:
* 用户数据接口 
* Version:1.0.0  
* @author pancm
* @date 2018年1月9日
 */
public interface UserDao extends JpaRepository<User, Long>{
	
}
