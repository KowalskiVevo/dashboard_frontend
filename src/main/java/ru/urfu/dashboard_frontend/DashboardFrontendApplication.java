package ru.urfu.dashboard_frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DashboardFrontendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DashboardFrontendApplication.class, args);
	}

}
