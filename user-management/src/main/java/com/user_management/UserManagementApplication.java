package com.user_management;

import com.user_management.model.Role;
import com.user_management.model.User;
import com.user_management.repository.UserRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@OpenAPIDefinition(
		info = @Info(
				title = "NEUQUIP - Spring Boot REST API Documentation - USER MANAGEMENT SERVICE",
				description = "NEUQUIP -  Spring Boot REST API Documentation",
				version = "v1.0",
				license = @License(
						name = "Apache 2.0",
						url = "https://www.apache.org/licenses/LICENSE-2.0"
				)
		)
)
@SpringBootApplication
public class UserManagementApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(UserManagementApplication.class, args);
	}

	@Override
	public void run(String... args) {
		if (userRepository.findByRole(Role.ROLE_ADMIN).isEmpty()) {
			User user = new User();
			user.setName("Mudasir");
			user.setEmail("mbhat@gmail.com");
			user.setRole(Role.ROLE_ADMIN);
			user.setPassword(passwordEncoder.encode("mbhat@admin"));
			user.setEnabled(true);
			user.setOrganization("Admin Org");
			user.setProfileImg(null);
			userRepository.save(user);
		}
	}
}
