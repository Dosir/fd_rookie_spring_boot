package com.fd.rookie.spring.boot;

import com.fd.rookie.spring.boot.mapper.UserMapper;
import com.fd.rookie.spring.boot.po.SecUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RookieSpringBootApplicationTests {
	private static final Logger log = LoggerFactory.getLogger(RookieSpringBootApplicationTests.class);

	@Autowired
	private UserMapper userMapper;

	@Test
	public void contextLoads() {
	}

	@Test
	public void test1() {
		final List<SecUser> userList = userMapper.findByUsername("test1");
		for (SecUser user : userList) {
			System.out.println(user.getUserId());
		}
	}

}
