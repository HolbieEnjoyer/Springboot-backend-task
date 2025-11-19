package com.seriousemployee.backendtask;

import com.seriousemployee.backendtask.dto.SuperAdminDTO;
import com.seriousemployee.backendtask.entities.Employee;
import com.seriousemployee.backendtask.repositories.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BackendtaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendtaskApplication.class, args);
	}

	@Bean
	public CommandLineRunner initSuperAdmin(EmployeeRepository repo, PasswordEncoder passwordEncoder, SuperAdminDTO superAdminDTO) {
		return args -> {
			String email = superAdminDTO.getUsername();
			String password = passwordEncoder.encode(superAdminDTO.getPassword());

			if (repo.findByEmail(email).isEmpty()) {
				Employee superadmin = new Employee(
						"Super Admin",
						email,
						password,
						"SUPERADMIN"
				);

				repo.save(superadmin);
			}
		};
	}
}
