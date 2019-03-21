package com.fd.rookie.spring.boot;

import com.fd.rookie.spring.boot.mapper.SecPermissionMapper;
import com.fd.rookie.spring.boot.mapper.UserMapper;
import com.fd.rookie.spring.boot.po.SecPermission;
import com.fd.rookie.spring.boot.po.SecUser;
import com.fd.rookie.spring.boot.utils.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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

	@Autowired
	private SecPermissionMapper secPermissionMapper;

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

	@Test
	public void getSecPermission() {
		SecPermission secPermission = secPermissionMapper.selectByPrimaryKey(1);
		System.out.println(secPermission.getPermissionName());
	}

	@Test
	public void testPageHelper() {
		PageHelper.startPage(1, 10).setOrderBy("permission_id desc");
		final PageInfo<SecPermission> permissionPageInfo = new PageInfo<>(this.secPermissionMapper.selectAll());
		log.info("[普通写法] - [{}]", permissionPageInfo);
	}

	@Test
	public void testPageInterceptor() {
		Page<SecPermission> permissionPage = new Page<>();
		permissionPage.setPageSize(2);
		permissionPage.setResults(secPermissionMapper.getSecPermissionList(permissionPage));
		System.out.println(permissionPage);
	}

}
