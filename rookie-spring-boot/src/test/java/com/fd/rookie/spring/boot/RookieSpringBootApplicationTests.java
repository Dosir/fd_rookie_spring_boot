package com.fd.rookie.spring.boot;

import com.fd.rookie.spring.boot.config.rabbit.MsgProducer;
import com.fd.rookie.spring.boot.mapper.SecPermissionMapper;
import com.fd.rookie.spring.boot.mapper.UserMapper;
import com.fd.rookie.spring.boot.po.SecPermission;
import com.fd.rookie.spring.boot.po.SecUser;
import com.fd.rookie.spring.boot.po.order.TOrder;
import com.fd.rookie.spring.boot.service.order.OrderService;
import com.fd.rookie.spring.boot.utils.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RookieSpringBootApplicationTests {
	private static final Logger log = LoggerFactory.getLogger(RookieSpringBootApplicationTests.class);

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private SecPermissionMapper secPermissionMapper;

	@Autowired
	private OrderService orderService;

	@Autowired
	MsgProducer producer;

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
		SecPermission secPermission = secPermissionMapper.selectByPrimaryKey(2);
		System.out.println(secPermission.getPermissionName());
	}

	@Test
	public void insertSecPermission() {
		SecPermission secPermission = new SecPermission();
		secPermission.setPermissionId(1);
		secPermission.setDescription("权限1");
		secPermission.setPermissionName("父权限");
		secPermissionMapper.insert(secPermission);
	}

	@Test
	public void testPageHelper() {
		PageHelper.startPage(1, 10).setOrderBy("permission_id desc");
		final PageInfo<SecPermission> permissionPageInfo = new PageInfo<>(this.secPermissionMapper.selectAll());
		log.info("[普通写法] - [{}]", permissionPageInfo);
	}

	/**
	 * 测试 分页拦截器
	 */
	@Test
	public void testPageInterceptor() {
		Page<SecPermission> permissionPage = new Page<>();
		permissionPage.setPageSize(2);
		permissionPage.setResults(secPermissionMapper.getSecPermissionList(permissionPage));
		System.out.println(permissionPage);
	}

	/**
	 * 测试 RabbitMQ
	 */
	@Test
	public void testRabbitMq() {
		String abc = "hello RabbitMQ!";
		producer.sendMsg(abc);
	}

	/**
	 * 测试 Rabbit的延迟队列
	 * @throws IOException
	 */
	@Test
	public void testRabbitMqDelay() throws IOException {
		String abc = "hello Delay RabbitMQ!";
		producer.sendDelayMsg(abc);
		System.in.read();
	}

	/**
	 * 测试策略模式
	 */
	@Test
	public void testStrategy() {
		TOrder tOrder = new TOrder();
		tOrder.setType("2");
		String result = orderService.handleOrder(tOrder);
		System.out.println(result);
	}

}
