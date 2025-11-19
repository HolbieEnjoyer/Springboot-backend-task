package com.seriousemployee.backendtask.security;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Employee Management API")
                        .version("1.0.0")
                        .description("""
                                # Employee Management API
                                
                                A comprehensive RESTful API for managing employee records with role-based access control.
                                
                                ## Features
                                - 🔐 JWT-based authentication
                                - 👥 Role-based access control (USER, ADMIN, SUPERADMIN)
                                - 📝 Complete CRUD operations
                                - 🔍 Advanced filtering and pagination
                                - 🛡️ Protected super admin account
                                
                                ## Authentication
                                1. Register a new account via `/api/v1/auth/register`
                                2. Login via `/api/v1/auth/login` to receive a JWT token
                                3. Include the token in the `Authorization` header as `Bearer <token>`
                                
                                ## Roles & Permissions
                                
                                | Role | Permissions |
                                |------|-------------|
                                | **USER** | View/update own profile, delete own account, view other employees |
                                | **ADMIN** | All USER permissions + delete USER accounts + promote to ADMIN |
                                | **SUPERADMIN** | All ADMIN permissions + delete/demote ADMIN accounts. Inserted into the db automatically at the startup. Cannot be modified. |
                                
                                You can find the super admin credentials on the <a href="https://github.com/HolbieEnjoyer/Springboot-backend-task?tab=readme-ov-file#user-credentials-in-prod">github repository</a> README.
                                
                                ```
                                """)
                        .contact(new Contact()
                                .name("API Support")
                                .url("https://github.com/HolbieEnjoyer/Springboot-backend-task")))
                .servers(List.of(
                        new Server().url("http://localhost:10030").description("Local Development"),
                        new Server().url("https://your-production-url.com").description("Production")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                                .description("Enter your JWT token in the format: Bearer <token>")))
                .tags(List.of(
                        new Tag()
                                .name("Authentication")
                                .description("Endpoints for user registration and authentication. No token required."),
                        new Tag()
                                .name("Employee Management")
                                .description("Endpoints for managing employee records. Requires authentication token.")
                ));
    }
}