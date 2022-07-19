package com.yyz.Yangtze_MANAGER;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yyz.Yangtze_MANAGER.mapper")
public class YangtzeManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(YangtzeManagerApplication.class, args);
	}

}
