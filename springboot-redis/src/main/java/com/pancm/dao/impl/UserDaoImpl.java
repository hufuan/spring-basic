package com.pancm.dao.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.pancm.dao.UserDao;
import com.pancm.pojo.User;
import com.pancm.util.RedisUtil;

/**
* @Title: UserDaoImpl
* @Description: 
* @Version:1.0.0  
* @author pancm
* @date 2018年8月15日
*/
@Repository
public class UserDaoImpl implements UserDao {

	@Resource
	private RedisUtil redisUtil;
	
	/** 
	 * 
	 */
	@Override
	public void addUser(User user) {
		redisUtil.set(String.valueOf(user.getId()), user.toString());
	}

	/** 
	 * 
	 */ 
	@Override
	public void updateUser(User user) {
		redisUtil.set(String.valueOf(user.getId()), user.toString());
	}

	/** 
	 * 
	 */
	@Override
	public void deleteUser(int id) {
		redisUtil.del(String.valueOf(id));
	}

	/** 
	 * 
	 */
	@Override
	public User findByUserId(int id) {
		System.out.println("findByUserId entry, id=" + id);
		System.out.println("redisUtil.get(String.valueOf(id)) == null as " + (redisUtil.get(String.valueOf(id)) == null));
		String data = redisUtil.get(String.valueOf(id)).toString();
		System.out.println("data=" + data);
		User user = JSON.parseObject(data, User.class);
		System.out.println("user=" + user);
		return  user;
	}

}
