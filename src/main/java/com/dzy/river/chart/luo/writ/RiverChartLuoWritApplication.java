package com.dzy.river.chart.luo.writ;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.dzy.river.chart.luo.writ.mapper")
public class RiverChartLuoWritApplication {

	public static void main(String[] args) {
		SpringApplication.run(RiverChartLuoWritApplication.class, args);
	}

}
