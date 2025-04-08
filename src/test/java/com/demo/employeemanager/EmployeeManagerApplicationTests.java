package com.demo.employeemanager;

import com.demo.employeemanager.models.entities.UserEntity;
import com.demo.employeemanager.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmployeeManagerApplicationTests {

	@Autowired
	JwtService jwtService;

	@Test
	void contextLoads() {

		UserEntity user = new UserEntity(23L, "parthashum@", "Parth", "pass");
		String token = jwtService.generateToken(user);
		Long id = jwtService.generateUserIdFromToken(token);
		System.out.println("Id: "+ id + " "+ token);

	}

}
