package com.airtribe.meditrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.modelmapper.ModelMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;

/**
 * MediTrack - Clinic & Appointment Management System
 * 
 * Main Spring Boot Application Entry Point
 * 
 * This application demonstrates:
 * - RESTful API design with Spring Boot
 * - JPA/Hibernate with H2 database
 * - OOP principles (Encapsulation, Inheritance, Polymorphism, Abstraction)
 * - Design Patterns (Singleton, Factory, Strategy, Observer)
 * - Exception handling and validation
 * - API documentation with Swagger/OpenAPI
 * 
 * @author Sudharma
 * @version 1.0.0
 * @since 2026-02-12
 */
@SpringBootApplication
public class MediTrackApplication {

    /**
     * Main method - Application entry point
     * 
     * @param args Command line arguments
     *             --loadData: Load sample data on startup
     *             --export: Export data to CSV
     */
    public static void main(String[] args) {
        // Print startup banner
        printBanner();

        // Start Spring Boot application
        SpringApplication.run(MediTrackApplication.class, args);

        System.out.println("\n✅ MediTrack Application Started Successfully!");
        System.out.println("📊 H2 Console: http://localhost:8080/h2-console");
        System.out.println("📚 Swagger UI: http://localhost:8080/swagger-ui.html");
        System.out.println("🔗 API Docs: http://localhost:8080/api-docs\n");
    }

    /**
     * ModelMapper Bean for DTO conversions
     * Demonstrates Singleton pattern (Spring manages as singleton by default)
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * OpenAPI Configuration for Swagger Documentation
     */
    @Bean
    public OpenAPI mediTrackOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MediTrack API")
                        .description("Clinic & Appointment Management System REST API")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Airtribe")
                                .email("support@airtribe.com")
                                .url("https://airtribe.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }

    /**
     * Print application startup banner
     */
    private static void printBanner() {
        System.out.println("\n" +
                "███╗   ███╗███████╗██████╗ ██╗████████╗██████╗  █████╗  ██████╗██╗  ██╗\n" +
                "████╗ ████║██╔════╝██╔══██╗██║╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██║ ██╔╝\n" +
                "██╔████╔██║█████╗  ██║  ██║██║   ██║   ██████╔╝███████║██║     █████╔╝ \n" +
                "██║╚██╔╝██║██╔══╝  ██║  ██║██║   ██║   ██╔══██╗██╔══██║██║     ██╔═██╗ \n" +
                "██║ ╚═╝ ██║███████╗██████╔╝██║   ██║   ██║  ██║██║  ██║╚██████╗██║  ██╗\n" +
                "╚═╝     ╚═╝╚══════╝╚═════╝ ╚═╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝\n" +
                "                  Clinic & Appointment Management System v1.0.0\n");
    }
}
