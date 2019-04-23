package com.fd.rookie.spring.boot;

import com.fd.rookie.spring.boot.common.Result;
import com.fd.rookie.spring.boot.config.exception.BusinessException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @RestController 等同于 （@Controller 与 @ResponseBody）
 */
@RestController
@SpringBootApplication
@tk.mybatis.spring.annotation.MapperScan(basePackages = "com.fd.rookie.spring.boot.mapper")
public class RookieSpringBootApplication {

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
	public Result testException(int num) throws RuntimeException{
		Result result = new Result();
		if (num < 0) {
			throw new BusinessException("num不能为负数!");
		}
		int i = 10 / num;
		result.setData(i);
		return result;
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
