package com.fd.rookie.spring.boot;

import com.fd.rookie.spring.boot.annotation.DateTime;
import com.fd.rookie.spring.boot.common.Result;
import com.fd.rookie.spring.boot.config.exception.BusinessException;
import com.fd.rookie.spring.boot.po.order.TOrder;
import com.fd.rookie.spring.boot.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.Arrays;

/**
 * @RestController 等同于 （@Controller 与 @ResponseBody）
 */
@RestController
@SpringBootApplication
@tk.mybatis.spring.annotation.MapperScan(basePackages = "com.fd.rookie.spring.boot.mapper")
@Validated
public class RookieSpringBootApplication {
	@Autowired
	private OrderService orderService;

	public static void main(String[] args) {
		SpringApplication.run(RookieSpringBootApplication.class, args);
	}

	@GetMapping("/demo1")
	public String demo1() {
		return "Hello Luis";
	}

	/**
	 * 测试 统一异常处理
	 */
	@GetMapping("/testException")
	public Result testException(@RequestParam @Min(value = 0, message = "num不能为负数.") int num) throws RuntimeException{
		Result result = new Result();
		if (num < 0) {
			throw new BusinessException("num不能为负数!");
		}
		int i = 10 / num;
		result.setData(i);
		return result;
	}

	@PostMapping("/testValidation")
	public void testValidation(@Validated TOrder tOrder) {
		String result = orderService.handleOrder(tOrder);
		System.out.println(result);
	}

	/**
	 * 测试自定义校验
	 */
	@GetMapping("/testValidation2")
	public String testValidation2(@RequestParam @DateTime(message = "您输入的格式错误，正确的格式为：{format}", format = "yyyy-MM-dd HH:mm") String date) {
		return "success";
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		// 目的是
		return args -> {
			System.out.println("来看看 SpringBoot 默认为我们提供的 Bean：");
			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			Arrays.stream(beanNames).forEach(System.out::println);
		};
	}
}
