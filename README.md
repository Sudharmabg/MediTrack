# MediTrack - Clinic & Appointment Management System

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![H2 Database](https://img.shields.io/badge/H2-Database-blue.svg)](https://www.h2database.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A comprehensive **Spring Boot REST API** application for managing clinic operations, appointments, doctors, patients, and billing. Built as a learning project to demonstrate Java fundamentals, OOP principles, design patterns, and modern enterprise architecture.

---

## 📋 Table of Contents

- [Features](#-features)
- [Learning Objectives](#-learning-objectives)
- [Technology Stack](#-technology-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Database Schema](#-database-schema)
- [Design Patterns](#-design-patterns)
- [Testing](#-testing)
- [Documentation](#-documentation)

---

## Features

### Core Functionality
-  **Doctor Management**: CRUD operations, search by specialization, availability management
-  **Patient Management**: Registration, medical history, appointment tracking
-  **Appointment Scheduling**: Book, confirm, cancel, reschedule appointments
-  **Billing System**: Generate bills, apply discounts, process payments
-  **Search & Filter**: Advanced search across doctors and patients
-  **Analytics**: Revenue reports, appointment statistics

### Technical Features
-  **RESTful API** with proper HTTP methods and status codes
-  **H2 In-Memory Database** with JPA/Hibernate
-  **Bean Validation** for request validation
-  **Global Exception Handling** with structured error responses
-  **Swagger/OpenAPI** documentation
-  **DTO Pattern** for API layer separation
-  **Transaction Management**
-  **Logging** with SLF4J

---

## Learning Objectives

This project demonstrates proficiency in:

### 1. **Java Fundamentals**
- JDK, JRE, JVM internals
- Access modifiers and variable scopes
- Primitive types and casting
- Static blocks and initialization

### 2. **Core OOP Principles**
- **Encapsulation**: Private fields with getters/setters, centralized validation
- **Inheritance**: `Person` → `Doctor`, `Patient` hierarchy
- **Polymorphism**: Method overloading and overriding, dynamic dispatch
- **Abstraction**: Abstract classes and interfaces

### 3. **Advanced OOP**
- **Deep vs Shallow Copy**: `Cloneable` implementation
- **Immutability**: `BillSummary` immutable class
- **Enums**: `Specialization`, `AppointmentStatus`, `Gender`
- **Static Initialization**: Application-wide configuration

### 4. **Collections & Generics**
- ArrayList, HashMap usage
- Custom generic `DataStore<T>` (optional)
- Comparators and iterators
- equals/hashCode contracts

### 5. **Exception Handling**
- Custom exception hierarchy (`MediTrackException`)
- Exception chaining
- Try-with-resources (CSV operations)
- Global exception handling with `@ControllerAdvice`

### 6. **Design Patterns**
- **Singleton**: Spring beans (managed by container)
- **Factory**: Bill creation strategies
- **Strategy**: Multiple billing strategies
- **Observer**: Appointment notifications (optional)
- **DTO Pattern**: Request/Response separation

### 7. **Java 8+ Features**
- **Streams API**: Filtering, mapping, sorting, aggregations
- **Lambdas**: Functional programming
- **Optional**: Null-safe operations
- **Default Methods**: Interface enhancements

### 8. **Spring Boot & Enterprise**
- Dependency Injection
- JPA/Hibernate ORM
- Repository pattern
- Service layer architecture
- REST controllers
- Transaction management

---

## Technology Stack

| Layer | Technology |
|-------|-----------|
| **Backend** | Java 17, Spring Boot 3.2.2 |
| **Database** | H2 (in-memory) |
| **ORM** | Spring Data JPA, Hibernate |
| **Validation** | Bean Validation (Hibernate Validator) |
| **Documentation** | SpringDoc OpenAPI 3 (Swagger) |
| **Build Tool** | Maven |
| **Logging** | SLF4J + Logback |
| **Utilities** | Lombok, ModelMapper |

---

## Project Structure

```
src/main/java/com/airtribe/meditrack/
├── MediTrackApplication.java          # Main application class
├── constants/
│   └── Constants.java                 # Application constants
├── enums/
│   ├── Specialization.java            # Doctor specializations
│   ├── AppointmentStatus.java         # Appointment states
│   └── Gender.java                    # Gender enum
├── entity/
│   ├── Person.java                    # Abstract base class
│   ├── Doctor.java                    # Doctor entity (extends Person)
│   ├── Patient.java                   # Patient entity (extends Person)
│   ├── Appointment.java               # Appointment entity
│   ├── Bill.java                      # Bill entity
│   └── BillSummary.java               # Immutable bill summary
├── dto/
│   ├── DoctorRequestDTO.java          # Doctor request DTO
│   ├── DoctorResponseDTO.java         # Doctor response DTO
│   ├── PatientRequestDTO.java         # Patient request DTO
│   ├── PatientResponseDTO.java        # Patient response DTO
│   ├── AppointmentRequestDTO.java     # Appointment request DTO
│   ├── AppointmentResponseDTO.java    # Appointment response DTO
│   ├── BillRequestDTO.java            # Bill request DTO
│   └── BillResponseDTO.java           # Bill response DTO
├── repository/
│   ├── DoctorRepository.java          # Doctor data access
│   ├── PatientRepository.java         # Patient data access
│   ├── AppointmentRepository.java     # Appointment data access
│   └── BillRepository.java            # Bill data access
├── service/
│   ├── DoctorService.java             # Doctor business logic
│   ├── PatientService.java            # Patient business logic
│   ├── AppointmentService.java        # Appointment business logic
│   └── BillService.java               # Billing business logic
├── controller/
│   ├── DoctorController.java          # Doctor REST API
│   ├── PatientController.java         # Patient REST API
│   ├── AppointmentController.java     # Appointment REST API
│   └── BillController.java            # Billing REST API
├── exception/
│   ├── MediTrackException.java        # Base exception
│   ├── ResourceNotFoundException.java # Resource not found
│   ├── AppointmentNotFoundException.java
│   ├── InvalidDataException.java      # Validation exception
│   └── GlobalExceptionHandler.java    # Global error handler
├── interfaces/
│   ├── Searchable.java                # Search contract
│   └── Payable.java                   # Payment contract
└── util/
    ├── Validator.java                 # Custom validators
    ├── DateUtil.java                  # Date utilities
    ├── CSVUtil.java                   # CSV import/export
    └── IdGenerator.java               # ID generation

docs/
├── JVM_Report.md                      # JVM architecture documentation
├── Setup_Instructions.md              # Setup guide
├── Design_Decisions.md                # Architecture decisions
└── API_Documentation.md               # API usage guide
```

---

## Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **Git**

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Sudharmabg/meditrack.git
   cd meditrack
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - **API Base URL**: `http://localhost:8080`
   - **Swagger UI**: `http://localhost:8080/swagger-ui.html`
   - **H2 Console**: `http://localhost:8080/h2-console`
     - JDBC URL: `jdbc:h2:mem:meditrackdb`
     - Username: `sa`
     - Password: *(leave blank)*

---

## API Documentation

### Base URL
```
http://localhost:8080/api/v1
```

### Endpoints

#### Doctor Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/doctors` | Create new doctor |
| GET | `/doctors` | Get all doctors |
| GET | `/doctors/{id}` | Get doctor by ID |
| PUT | `/doctors/{id}` | Update doctor |
| DELETE | `/doctors/{id}` | Delete doctor |
| GET | `/doctors/search?keyword={keyword}` | Search doctors |
| GET | `/doctors/specialization/{spec}` | Get by specialization |

#### Patient Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/patients` | Register new patient |
| GET | `/patients` | Get all patients |
| GET | `/patients/{id}` | Get patient by ID |
| PUT | `/patients/{id}` | Update patient |
| DELETE | `/patients/{id}` | Delete patient |
| GET | `/patients/search?keyword={keyword}` | Search patients |

#### Appointment Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/appointments` | Book appointment |
| GET | `/appointments` | Get all appointments |
| GET | `/appointments/{id}` | Get appointment by ID |
| PUT | `/appointments/{id}/confirm` | Confirm appointment |
| PUT | `/appointments/{id}/cancel` | Cancel appointment |
| PUT | `/appointments/{id}/complete` | Complete appointment |
| GET | `/appointments/doctor/{doctorId}` | Get doctor's appointments |
| GET | `/appointments/patient/{patientId}` | Get patient's appointments |

#### Billing Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/bills` | Generate bill |
| GET | `/bills/{id}` | Get bill by ID |
| PUT | `/bills/{id}/pay` | Process payment |
| GET | `/bills/unpaid` | Get unpaid bills |
| GET | `/bills/revenue` | Get revenue statistics |

### Sample Request

**Create Doctor**
```bash
curl -X POST http://localhost:8080/api/v1/doctors \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Rajesh",
    "lastName": "Kumar",
    "phoneNumber": "9876543210",
    "email": "dr.rajesh@meditrack.com",
    "gender": "MALE",
    "dateOfBirth": "1980-05-15",
    "address": "123 MG Road, Bangalore",
    "licenseNumber": "MCI-12345",
    "specialization": "CARDIOLOGIST",
    "consultationFee": 1500.0,
    "experienceYears": 15,
    "qualifications": "MBBS, MD (Cardiology)",
    "available": true
  }'
```

---

## 🗄 Database Schema

### Entity Relationships

```
Person (Abstract)
├── Doctor (1) ──────< (N) Appointment
└── Patient (1) ─────< (N) Appointment

Appointment (1) ────< (1) Bill
```

### Key Tables

- **persons**: Base table for Person hierarchy
- **doctors**: Doctor-specific fields (JOINED inheritance)
- **patients**: Patient-specific fields (JOINED inheritance)
- **appointments**: Appointment records
- **bills**: Billing information

---

## 🎨 Design Patterns

### 1. **Singleton Pattern**
- Spring beans are managed as singletons by the IoC container
- Example: `ModelMapper`, Services

### 2. **Factory Pattern**
- Bill creation with different strategies
- DTO mapping

### 3. **Strategy Pattern**
- Multiple billing calculation strategies
- Payment processing strategies

### 4. **Observer Pattern** (Optional)
- Appointment notification system
- Event-driven architecture

### 5. **Repository Pattern**
- Data access abstraction
- Spring Data JPA repositories

### 6. **DTO Pattern**
- Separation of API layer from domain layer
- Request/Response DTOs

---

## 🧪 Testing

MediTrack includes a comprehensive test suite that demonstrates all features and design patterns.

### Comprehensive TestRunner

The application includes an **automated TestRunner** that executes when you start the application. It demonstrates:

-  **Doctor Registration** - Creates sample doctors with different specializations
-  **Patient Registration** - Registers sample patients with medical history
-  **Appointment Booking** - Books appointments and triggers notifications (Observer Pattern)
-  **Search & Filtering** - Tests search functionality across entities
-  **Statistics & Analytics** - Generates reports and analytics

#### Running the TestRunner

**Method 1: Automatic Execution (Default)**

The TestRunner executes automatically when you start the application:

```bash
# Start the application
mvn spring-boot:run
```

**Expected Output:**
```
================================================================================
MEDITRACK COMPREHENSIVE TEST RUNNER
================================================================================

Testing all features and design patterns...

--------------------------------------------------------------------------------
  Test 1: Doctor Registration
--------------------------------------------------------------------------------

Registered Doctor 1: Dr. Sarah Johnson (ID: 1)
Specialization: CARDIOLOGIST
Fee: ₹1500.0
Experience: 15 years
Registered Doctor 2: Dr. Michael Chen (ID: 2)
Registered Doctor 3: Dr. Priya Sharma (ID: 3)

--------------------------------------------------------------------------------
  Test 2: Patient Registration
--------------------------------------------------------------------------------

Registered Patient 1: John Doe (ID: 1)
Age: 38 years
Blood Group: O+
Allergies: Penicillin
Registered Patient 2: Jane Smith (ID: 2)
Registered Patient 3: Raj Kumar (ID: 3)

--------------------------------------------------------------------------------
  Test 3: Appointment Booking (Observer Pattern)
--------------------------------------------------------------------------------

Booked Appointment 1: ID 1
Doctor: Dr. Sarah Johnson
Patient: John Doe
Date/Time: 2026-02-14T10:00
Status: SCHEDULED
Email notification sent!
SMS notification sent!
Booked Appointment 2: ID 2
Booked Appointment 3: ID 3

--------------------------------------------------------------------------------
  Test 4: Search and Filtering
--------------------------------------------------------------------------------

Testing search capabilities:
Found 1 Cardiologist(s)
Found 3 available doctor(s)
Found 3 active patient(s)

--------------------------------------------------------------------------------
  Test 5: Statistics and Analytics
--------------------------------------------------------------------------------

Doctor Statistics:
   Total Doctors: 3
   Available Doctors: 3
   Average Consultation Fee: ₹1233.33

Patient Statistics:
   Total Patients: 3
   Active Patients: 3

Appointment Statistics:
   Total Appointments: 3

================================================================================
  ALL TESTS COMPLETED SUCCESSFULLY! 
================================================================================

You can now access:
Swagger UI: http://localhost:8080/swagger-ui.html
H2 Console: http://localhost:8080/h2-console
API Docs: http://localhost:8080/api-docs

Application is running. Press Ctrl+C to stop.
```

**Method 2: Disable TestRunner**

To disable automatic test execution, comment out the `@Component` annotation:

```java
// File: src/main/java/com/airtribe/meditrack/runner/TestRunner.java

// @Component  // Comment this line to disable
public class TestRunner implements CommandLineRunner {
    // ...
}
```

### Manual API Testing

#### Using cURL

```bash
# Test Doctor Creation
curl -X POST http://localhost:8080/api/v1/doctors \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Test",
    "lastName": "Doctor",
    "email": "test.doctor@hospital.com",
    "phoneNumber": "9876543210",
    "dateOfBirth": "1980-01-01",
    "gender": "MALE",
    "address": "Test Address",
    "specialization": "CARDIOLOGIST",
    "licenseNumber": "TEST123",
    "consultationFee": 1500.0,
    "experienceYears": 10,
    "qualifications": "MBBS, MD",
    "available": true
  }'

# Test Get All Doctors
curl http://localhost:8080/api/v1/doctors

# Test Get Doctor by ID
curl http://localhost:8080/api/v1/doctors/1
```

#### Using Swagger UI

1. Start the application: `mvn spring-boot:run`
2. Open Swagger UI: `http://localhost:8080/swagger-ui.html`
3. Expand any endpoint (e.g., **POST /api/v1/doctors**)
4. Click **"Try it out"**
5. Fill in the request body
6. Click **"Execute"**
7. View the response

#### Using Postman

1. Import the API collection (if available)
2. Set base URL: `http://localhost:8080/api/v1`
3. Create requests for each endpoint
4. Test different scenarios

#### H2 Database Issues

```bash
# Ensure H2 console is enabled in application.properties
spring.h2.console.enabled=true

# Check database URL
spring.datasource.url=jdbc:h2:mem:meditrackdb
```

## 📖 Documentation

### Generated Documentation

1. **JavaDoc**
   ```bash
   mvn javadoc:javadoc
   ```
   Output: `target/site/apidocs/index.html`

2. **Swagger UI**
   - Access at: `http://localhost:8080/swagger-ui.html`
   - Interactive API documentation
   - Test endpoints directly from browser

### Project Documentation

Comprehensive documentation is available in the `docs/` folder:

- **[Setup Instructions](docs/Setup_Instructions.md)**: Complete setup guide with prerequisites, installation steps, configuration options, and troubleshooting
- **[Design Decisions](docs/Design_Decisions.md)**: Architecture overview, design patterns, technology choices, and rationale behind key decisions
- **[JVM Report](docs/JVM_Report.md)**: JVM architecture, memory management, and internals
- **[UML Class Diagram](docs/UML_Class_Diagram.md)**: Visual representation of class relationships and system architecture

### Quick Links

| Document | Purpose | Key Topics |
|----------|---------|------------|
| **Setup Instructions** | Getting started guide | Prerequisites, Installation, Configuration, Running Tests |
| **Design Decisions** | Architecture documentation | Design Patterns, Technology Stack, Database Design, API Design |
| **JVM Report** | Java internals | JVM Architecture, Memory Areas, Garbage Collection, WORA |
| **UML Diagram** | Visual architecture | Class Relationships, Inheritance, Associations |
| **README** | Project overview | Features, API Endpoints, Quick Start |

---

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## Authors

- **Sudharma**
- Email: bgsudharma1998@airtribe.com

---

## Acknowledgments

- Spring Boot Documentation
- Java Design Patterns
- Airtribe AI-First Backend Engineering Launchpad

---

**Built with ❤️ by Sudharma**
