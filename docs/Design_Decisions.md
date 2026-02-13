# MediTrack Design Decisions

## Table of Contents
1. [Architecture Overview](#architecture-overview)
2. [Design Patterns](#design-patterns)
3. [Technology Choices](#technology-choices)
4. [Database Design](#database-design)
5. [API Design](#api-design)
6. [Exception Handling Strategy](#exception-handling-strategy)
7. [Validation Strategy](#validation-strategy)
8. [Testing Strategy](#testing-strategy)
9. [Performance Considerations](#performance-considerations)
10. [Security Considerations](#security-considerations)

---

## Architecture Overview

### Layered Architecture

MediTrack follows a **layered architecture** pattern to ensure separation of concerns and maintainability:

```
┌─────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                   │
│              (REST Controllers + DTOs)                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │   Doctor     │  │   Patient    │  │ Appointment  │ │
│  │ Controller   │  │  Controller  │  │  Controller  │ │
│  └──────────────┘  └──────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│                    SERVICE LAYER                        │
│              (Business Logic)                           │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │   Doctor     │  │   Patient    │  │ Appointment  │ │
│  │   Service    │  │   Service    │  │   Service    │ │
│  └──────────────┘  └──────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│                  PERSISTENCE LAYER                      │
│              (Data Access)                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │   Doctor     │  │   Patient    │  │ Appointment  │ │
│  │ Repository   │  │ Repository   │  │ Repository   │ │
│  └──────────────┘  └──────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│                    DATABASE LAYER                       │
│                  (H2 In-Memory DB)                      │
└─────────────────────────────────────────────────────────┘
```

### Why Layered Architecture?

| Benefit | Description |
|---------|-------------|
| **Separation of Concerns** | Each layer has a specific responsibility |
| **Maintainability** | Changes in one layer don't affect others |
| **Testability** | Each layer can be tested independently |
| **Reusability** | Services can be reused across controllers |
| **Scalability** | Easy to scale individual layers |

---

## Design Patterns

### 1. Singleton Pattern

**Implementation:** Spring Bean Management

**Rationale:**
- Spring manages all services, repositories, and controllers as singleton beans
- Ensures single instance per application context
- Reduces memory footprint
- Thread-safe by default

**Example:**
```java
@Service  // Spring creates a single instance
public class DoctorService {
    // Singleton managed by Spring IoC container
}
```

**Benefits:**
-  Automatic lifecycle management
-  Dependency injection support
-  Thread-safe
-  Memory efficient

---

### 2. Repository Pattern

**Implementation:** Spring Data JPA Repositories

**Rationale:**
- Abstracts data access logic
- Provides CRUD operations out-of-the-box
- Supports custom queries
- Database-agnostic

**Example:**
```java
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findBySpecialization(Specialization specialization);
    List<Doctor> findByAvailableTrue();
}
```

**Benefits:**
-  Reduces boilerplate code
-  Type-safe queries
-  Automatic transaction management
-  Easy to mock for testing

---

### 3. DTO Pattern (Data Transfer Object)

**Implementation:** Request and Response DTOs

**Rationale:**
- Separates API layer from domain layer
- Prevents over-exposure of entity details
- Allows different representations for input/output
- Supports API versioning

**Example:**
```java
// Request DTO - What client sends
public class DoctorRequestDTO {
    private String firstName;
    private String lastName;
    private Specialization specialization;
    // Only fields needed for creation
}

// Response DTO - What client receives
public class DoctorResponseDTO {
    private Long id;
    private String fullName;
    private Specialization specialization;
    private Integer appointmentCount;
    // Computed fields and formatted data
}
```

**Benefits:**
-  API stability (entity changes don't break API)
-  Security (hide sensitive fields)
-  Flexibility (different views of same data)
-  Validation at API boundary

---

### 4. Factory Pattern

**Implementation:** Bill Creation

**Rationale:**
- Encapsulates object creation logic
- Supports multiple bill types
- Easy to extend with new bill types

**Example:**
```java
public class BillFactory {
    public static Bill createBill(Appointment appointment, BillType type) {
        return switch (type) {
            case STANDARD -> new StandardBill(appointment);
            case INSURANCE -> new InsuranceBill(appointment);
            case EMERGENCY -> new EmergencyBill(appointment);
        };
    }
}
```

**Benefits:**
-  Centralized creation logic
-  Easy to add new bill types
-  Follows Open/Closed Principle

---

### 5. Strategy Pattern

**Implementation:** Billing Strategies

**Rationale:**
- Different billing calculation algorithms
- Runtime selection of strategy
- Easy to add new strategies

**Example:**
```java
public interface BillingStrategy {
    double calculateAmount(Appointment appointment);
}

public class StandardBillingStrategy implements BillingStrategy {
    @Override
    public double calculateAmount(Appointment appointment) {
        return appointment.getDoctor().getConsultationFee();
    }
}

public class DiscountBillingStrategy implements BillingStrategy {
    @Override
    public double calculateAmount(Appointment appointment) {
        double fee = appointment.getDoctor().getConsultationFee();
        return fee * 0.9; // 10% discount
    }
}
```

**Benefits:**
-  Flexible billing logic
-  Easy to test different strategies
-  Follows Single Responsibility Principle

---

### 6. Observer Pattern

**Implementation:** Appointment Notifications

**Rationale:**
- Decouple appointment booking from notifications
- Support multiple notification channels
- Easy to add new observers

**Example:**
```java
public interface AppointmentObserver {
    void onAppointmentBooked(Appointment appointment);
}

public class EmailNotificationObserver implements AppointmentObserver {
    @Override
    public void onAppointmentBooked(Appointment appointment) {
        sendEmail(appointment);
    }
}

public class SMSNotificationObserver implements AppointmentObserver {
    @Override
    public void onAppointmentBooked(Appointment appointment) {
        sendSMS(appointment);
    }
}
```

**Benefits:**
-  Loose coupling
-  Easy to add new notification channels
-  Asynchronous notification support

---

## Technology Choices

### 1. Spring Boot

**Why Spring Boot?**

| Feature | Benefit |
|---------|---------|
| **Auto-configuration** | Reduces boilerplate configuration |
| **Embedded Server** | No need for external Tomcat |
| **Starter Dependencies** | Simplified dependency management |
| **Production-ready** | Built-in health checks, metrics |
| **Large Ecosystem** | Extensive community support |

### 2. H2 Database

**Why H2?**

| Feature | Benefit |
|---------|---------|
| **In-memory** | Fast for development and testing |
| **Zero configuration** | No installation required |
| **Web Console** | Built-in database viewer |
| **Lightweight** | Minimal resource usage |
| **SQL Compliant** | Easy migration to production DB |

**Trade-offs:**
-  Data lost on restart (acceptable for learning project)
-  Not suitable for production
-  Perfect for development and testing

### 3. JPA/Hibernate

**Why JPA/Hibernate?**

| Feature | Benefit |
|---------|---------|
| **ORM** | Object-relational mapping |
| **Database Agnostic** | Easy to switch databases |
| **Lazy Loading** | Performance optimization |
| **Caching** | First and second-level caching |
| **Query DSL** | Type-safe queries |

### 4. Bean Validation

**Why Bean Validation?**

| Feature | Benefit |
|---------|---------|
| **Declarative** | Annotations on fields |
| **Reusable** | Same validation across layers |
| **Standard** | JSR-380 specification |
| **Extensible** | Custom validators |

**Example:**
```java
public class DoctorRequestDTO {
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @Email(message = "Invalid email format")
    private String email;
    
    @Min(value = 0, message = "Fee must be positive")
    private Double consultationFee;
}
```

### 5. Lombok

**Why Lombok?**

| Feature | Benefit |
|---------|---------|
| **@Data** | Auto-generates getters/setters |
| **@Builder** | Fluent builder pattern |
| **@Slf4j** | Automatic logger creation |
| **@NoArgsConstructor** | Default constructor |

**Trade-offs:**
-  Reduces boilerplate code by 60%
-  Cleaner, more readable code
-  IDE plugin required
-  Debugging can be harder

### 6. ModelMapper

**Why ModelMapper?**

| Feature | Benefit |
|---------|---------|
| **Automatic Mapping** | DTO ↔ Entity conversion |
| **Convention-based** | Matches fields by name |
| **Customizable** | Custom mappings when needed |

**Example:**
```java
@Service
public class DoctorService {
    @Autowired
    private ModelMapper modelMapper;
    
    public DoctorResponseDTO createDoctor(DoctorRequestDTO request) {
        Doctor doctor = modelMapper.map(request, Doctor.class);
        doctor = doctorRepository.save(doctor);
        return modelMapper.map(doctor, DoctorResponseDTO.class);
    }
}
```

---

## Database Design

### Entity Relationships

```
┌─────────────────────┐
│      Person         │ (Abstract)
│  ─────────────────  │
│  - id               │
│  - firstName        │
│  - lastName         │
│  - email            │
│  - phoneNumber      │
│  - dateOfBirth      │
│  - gender           │
│  - address          │
└─────────────────────┘
         ▲
         │ (Inheritance)
    ┌────┴────┐
    │         │
┌───┴───┐ ┌──┴────┐
│Doctor │ │Patient│
└───┬───┘ └──┬────┘
    │        │
    │        │
    └───┬────┘
        │
   ┌────▼────────┐
   │ Appointment │
   └────┬────────┘
        │
   ┌────▼────┐
   │  Bill   │
   └─────────┘
```

### Inheritance Strategy: JOINED

**Why JOINED inheritance?**

```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Common fields
}

@Entity
public class Doctor extends Person {
    private Specialization specialization;
    private String licenseNumber;
    // Doctor-specific fields
}
```

**Benefits:**
-  Normalized schema (no duplicate columns)
-  Referential integrity
-  Clear separation of concerns

**Trade-offs:**
-  Requires JOIN for queries (acceptable for small dataset)
-  Better data integrity than SINGLE_TABLE

### Enums for Type Safety

**Why Enums?**

```java
public enum Specialization {
    CARDIOLOGIST("Cardiology"),
    PEDIATRICIAN("Pediatrics"),
    DERMATOLOGIST("Dermatology"),
    NEUROLOGIST("Neurology");
    
    private final String displayName;
    
    Specialization(String displayName) {
        this.displayName = displayName;
    }
}
```

**Benefits:**
-  Type-safe (compile-time checking)
-  Prevents invalid values
-  Self-documenting code
-  Easy to extend

---

## API Design

### RESTful Principles

**Resource-based URLs:**
```
 GET    /api/v1/doctors          (Get all doctors)
 GET    /api/v1/doctors/{id}     (Get specific doctor)
 POST   /api/v1/doctors          (Create doctor)
 PUT    /api/v1/doctors/{id}     (Update doctor)
 DELETE /api/v1/doctors/{id}     (Delete doctor)

 /api/v1/getDoctors              (Not RESTful)
 /api/v1/createDoctor            (Not RESTful)
```

### HTTP Status Codes

| Status Code | Usage |
|-------------|-------|
| **200 OK** | Successful GET, PUT |
| **201 Created** | Successful POST |
| **204 No Content** | Successful DELETE |
| **400 Bad Request** | Validation error |
| **404 Not Found** | Resource not found |
| **500 Internal Server Error** | Server error |

### Versioning Strategy

**URL-based versioning:**
```
/api/v1/doctors
/api/v2/doctors  (future version)
```

**Why URL versioning?**
-  Simple and explicit
-  Easy to test different versions
-  Clear deprecation path

---

## Exception Handling Strategy

### Exception Hierarchy

```
MediTrackException (Base)
    ├── ResourceNotFoundException
    │   ├── DoctorNotFoundException
    │   ├── PatientNotFoundException
    │   └── AppointmentNotFoundException
    ├── InvalidDataException
    └── BusinessLogicException
```

### Global Exception Handler

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex) {
        // Handle validation errors
    }
}
```

**Benefits:**
-  Centralized error handling
-  Consistent error responses
-  Cleaner controller code

---

## Validation Strategy

### Multi-layer Validation

```
┌─────────────────────────────────────┐
│  Controller Layer                   │
│  - @Valid annotation                │
│  - Bean Validation                  │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│  Service Layer                      │
│  - Business logic validation        │
│  - Cross-field validation           │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│  Database Layer                     │
│  - Constraints (NOT NULL, UNIQUE)   │
└─────────────────────────────────────┘
```

**Example:**
```java
// Controller Layer
@PostMapping
public ResponseEntity<DoctorResponseDTO> createDoctor(
        @Valid @RequestBody DoctorRequestDTO request) {
    // Bean validation happens automatically
}

// Service Layer
public DoctorResponseDTO createDoctor(DoctorRequestDTO request) {
    // Business validation
    if (doctorRepository.existsByLicenseNumber(request.getLicenseNumber())) {
        throw new InvalidDataException("License number already exists");
    }
}
```

---

## Testing Strategy

### Test Pyramid

```
         ┌─────────┐
         │   E2E   │  (Few)
         └─────────┘
       ┌─────────────┐
       │ Integration │  (Some)
       └─────────────┘
     ┌─────────────────┐
     │   Unit Tests    │  (Many)
     └─────────────────┘
```

### Test Coverage

| Layer | Test Type | Coverage |
|-------|-----------|----------|
| **Service** | Unit Tests | 80%+ |
| **Repository** | Integration Tests | 70%+ |
| **Controller** | API Tests | 60%+ |

### TestRunner

**Purpose:**
- Demonstrates all features
- Validates design patterns
- Provides usage examples

**Example:**
```java
@Component
public class TestRunner implements CommandLineRunner {
    @Override
    public void run(String... args) {
        // Test 1: Doctor Registration
        // Test 2: Patient Registration
        // Test 3: Appointment Booking
        // Test 4: Search and Filtering
        // Test 5: Statistics
    }
}
```

---

## Performance Considerations

### 1. Lazy Loading

```java
@Entity
public class Doctor extends Person {
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<Appointment> appointments;
}
```

**Benefits:**
-  Loads appointments only when accessed
-  Reduces initial query time
-  Saves memory

### 2. Indexing

```java
@Entity
@Table(indexes = {
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_specialization", columnList = "specialization")
})
public class Doctor extends Person {
    // Faster queries on email and specialization
}
```

### 3. Pagination

```java
public Page<Doctor> getAllDoctors(Pageable pageable) {
    return doctorRepository.findAll(pageable);
}
```

**Benefits:**
-  Reduces memory usage
-  Faster response times
-  Better user experience

---

## Security Considerations

### Current Implementation (Learning Project)

| Feature | Status | Production Recommendation |
|---------|--------|---------------------------|
| **Authentication** |  Not implemented |  Spring Security + JWT |
| **Authorization** |  Not implemented |  Role-based access control |
| **Password Encryption** |  Not applicable |  BCrypt hashing |
| **HTTPS** |  Not configured |  SSL/TLS certificates |
| **Input Validation** |  Implemented |  Keep and enhance |
| **SQL Injection** |  Protected (JPA) |  Parameterized queries |

### Future Enhancements

```java
// Authentication
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/doctors/**").hasRole("DOCTOR")
                .anyRequest().authenticated()
            )
            .build();
    }
}
```

---

## Summary

### Key Design Decisions

| Decision | Rationale | Trade-off |
|----------|-----------|-----------|
| **Layered Architecture** | Separation of concerns | More classes, but better maintainability |
| **Spring Boot** | Rapid development | Learning curve for beginners |
| **H2 Database** | Zero configuration | Not for production |
| **DTO Pattern** | API stability | More boilerplate code |
| **Bean Validation** | Declarative validation | Limited complex validation |
| **Lombok** | Less boilerplate | IDE dependency |

### Design Principles Applied

-  **SOLID Principles**
  - Single Responsibility
  - Open/Closed
  - Liskov Substitution
  - Interface Segregation
  - Dependency Inversion

-  **DRY (Don't Repeat Yourself)**
-  **KISS (Keep It Simple, Stupid)**
-  **YAGNI (You Aren't Gonna Need It)**

---

## References

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Design Patterns (Gang of Four)](https://refactoring.guru/design-patterns)
- [RESTful API Design](https://restfulapi.net/)
- [JPA Best Practices](https://thorben-janssen.com/tips-to-boost-your-hibernate-performance/)

---

**Built with thoughtful design decisions for learning and scalability! 🏗️**
