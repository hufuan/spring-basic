package com.fuhu.demo.h2;

import com.fuhu.demo.h2.entity.Cat;
import com.fuhu.demo.h2.service.CatService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class H2ApplicationTests {

	@Autowired
	private CatService catService;

	@Test
	public void contextLoads() {
		List<Cat> list = catService.list();
		list.forEach(System.out::println);
	}

}
