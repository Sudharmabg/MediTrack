# MediTrack Setup Instructions

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Installation](#installation)
3. [Configuration](#configuration)
4. [Running the Application](#running-the-application)
5. [Accessing the Application](#accessing-the-application)
6. [Running Tests](#running-tests)
7. [Troubleshooting](#troubleshooting)
8. [IDE Setup](#ide-setup)

---

## Prerequisites

Before setting up MediTrack, ensure you have the following installed on your system:

### Required Software

| Software | Version | Download Link |
|----------|---------|---------------|
| **Java JDK** | 17 or higher | [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/) |
| **Maven** | 3.6+ | [Apache Maven](https://maven.apache.org/download.cgi) |
| **Git** | Latest | [Git Downloads](https://git-scm.com/downloads) |

### Optional Tools

| Software | Purpose |
|----------|---------|
| **IntelliJ IDEA** | Recommended IDE for Java development |
| **VS Code** | Alternative IDE with Java extensions |
| **Postman** | API testing tool |
| **DBeaver** | Database management tool (for H2 console alternative) |

### Verify Installation

```bash
# Check Java version
java -version
# Expected output: java version "17.x.x" or higher

# Check Maven version
mvn -version
# Expected output: Apache Maven 3.6.x or higher

# Check Git version
git --version
# Expected output: git version 2.x.x or higher
```

---

## Installation

### Step 1: Clone the Repository

```bash
# Clone the repository
git clone https://github.com/Sudharmabg/MediTrack.git

# Navigate to project directory
cd MediTrack
```

### Step 2: Build the Project

```bash
# Clean and build the project
mvn clean install

# This will:
# - Download all dependencies
# - Compile the source code
# - Run unit tests
# - Package the application as a JAR file
```

**Expected Output:**
```
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  XX.XXX s
[INFO] Finished at: YYYY-MM-DDTHH:MM:SS
[INFO] ------------------------------------------------------------------------
```

### Step 3: Verify Build

```bash
# Check if JAR file is created
ls target/

# You should see: meditrack-0.0.1-SNAPSHOT.jar
```

---

## Configuration

### Application Properties

The application uses default configuration from `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080

# Application Name
spring.application.name=MediTrack

# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:meditrackdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# H2 Console Configuration
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Logging Configuration
logging.level.com.airtribe.meditrack=DEBUG
logging.level.org.springframework.web=INFO
logging.level.org.hibernate.SQL=DEBUG
```

### Custom Configuration (Optional)

To customize the configuration, create a new file `application-dev.properties`:

```properties
# Custom port
server.port=9090

# Different database name
spring.datasource.url=jdbc:h2:mem:customdb

# Disable SQL logging
spring.jpa.show-sql=false
```

Run with custom profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## Running the Application

### Method 1: Using Maven (Recommended for Development)

```bash
# Run the application
mvn spring-boot:run
```

### Method 2: Using JAR File (Recommended for Production)

```bash
# Build the JAR first
mvn clean package -DskipTests

# Run the JAR
java -jar target/meditrack-0.0.1-SNAPSHOT.jar
```

### Method 3: Using IDE

#### IntelliJ IDEA
1. Open the project in IntelliJ IDEA
2. Navigate to `src/main/java/com/airtribe/meditrack/MediTrackApplication.java`
3. Right-click and select **Run 'MediTrackApplication'**

#### VS Code
1. Open the project in VS Code
2. Install **Extension Pack for Java**
3. Press `F5` or use **Run → Start Debugging**

### Application Startup

When the application starts successfully, you'll see:

```
  __  __          _ _ _____                _    
 |  \/  | ___  __| (_)_   _| __ __ _  ___| | __
 | |\/| |/ _ \/ _` | | | || '__/ _` |/ __| |/ /
 | |  | |  __/ (_| | | | || | | (_| | (__|   < 
 |_|  |_|\___|\__,_|_| |_||_|  \__,_|\___|_|\_\
                                                

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
...

================================================================================
  ALL TESTS COMPLETED SUCCESSFULLY! 
================================================================================

You can now access:
Swagger UI: http://localhost:8080/swagger-ui.html
H2 Console: http://localhost:8080/h2-console
API Docs: http://localhost:8080/api-docs

Application is running. Press Ctrl+C to stop.
```

---

## Accessing the Application

### 1. Swagger UI (API Documentation)

**URL:** `http://localhost:8080/swagger-ui.html`

- Interactive API documentation
- Test API endpoints directly from browser
- View request/response schemas

### 2. H2 Database Console

**URL:** `http://localhost:8080/h2-console`

**Connection Details:**
- **JDBC URL:** `jdbc:h2:mem:meditrackdb`
- **Username:** `sa`
- **Password:** *(leave blank)*

### 3. REST API Endpoints

**Base URL:** `http://localhost:8080/api/v1`

**Example Request:**
```bash
# Get all doctors
curl http://localhost:8080/api/v1/doctors

# Create a new doctor
curl -X POST http://localhost:8080/api/v1/doctors \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@hospital.com",
    "phoneNumber": "9876543210",
    "dateOfBirth": "1980-01-01",
    "gender": "MALE",
    "address": "123 Main St",
    "specialization": "CARDIOLOGIST",
    "licenseNumber": "LIC12345",
    "consultationFee": 1500.0,
    "experienceYears": 10,
    "qualifications": "MBBS, MD",
    "available": true
  }'
```

---

## Running Tests

### Run All Tests

```bash
# Run all unit and integration tests
mvn test
```

### Run Specific Test Class

```bash
# Run a specific test class
mvn test -Dtest=DoctorServiceTest

# Run multiple test classes
mvn test -Dtest=DoctorServiceTest,PatientServiceTest
```

### Run with Coverage Report

```bash
# Generate test coverage report
mvn clean test jacoco:report

# View report at: target/site/jacoco/index.html
```

### Test Runner

The application includes a comprehensive `TestRunner` that automatically executes when the application starts. It demonstrates:

- Doctor registration
- Patient registration
- Appointment booking
- Search and filtering
- Statistics and analytics

**To disable TestRunner:**

Comment out the `@Component` annotation in `TestRunner.java`:

```java
// @Component  // Comment this line
public class TestRunner implements CommandLineRunner {
    // ...
}
```

---

## Troubleshooting

### Common Issues

#### 1. Port Already in Use

**Error:**
```
Web server failed to start. Port 8080 was already in use.
```

**Solution:**
```bash
# Option 1: Kill the process using port 8080
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9

# Option 2: Change the port in application.properties
server.port=9090
```

#### 2. Java Version Mismatch

**Error:**
```
Unsupported class file major version XX
```

**Solution:**
```bash
# Check Java version
java -version

# Ensure Java 17 or higher is installed
# Update JAVA_HOME environment variable
```

#### 3. Maven Build Failure

**Error:**
```
[ERROR] Failed to execute goal
```

**Solution:**
```bash
# Clear Maven cache
mvn clean

# Update dependencies
mvn clean install -U

# Skip tests if needed
mvn clean install -DskipTests
```

#### 4. H2 Console Not Accessible

**Error:**
```
404 - H2 Console not found
```

**Solution:**

Verify `application.properties`:
```properties
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

#### 5. Database Connection Issues

**Error:**
```
Unable to connect to database
```

**Solution:**

Check H2 console settings:
- JDBC URL: `jdbc:h2:mem:meditrackdb`
- Username: `sa`
- Password: *(empty)*

---

## IDE Setup

### IntelliJ IDEA

#### 1. Import Project

1. **File → Open**
2. Select the `MediTrack` directory
3. Choose **Import as Maven project**
4. Wait for dependencies to download

#### 2. Configure JDK

1. **File → Project Structure → Project**
2. Set **Project SDK** to Java 17
3. Set **Language level** to 17

#### 3. Enable Lombok

1. **File → Settings → Plugins**
2. Search and install **Lombok**
3. **File → Settings → Build, Execution, Deployment → Compiler → Annotation Processors**
4. Enable **Enable annotation processing**

#### 4. Run Configuration

1. **Run → Edit Configurations**
2. Click **+** → **Spring Boot**
3. Set **Main class:** `com.airtribe.meditrack.MediTrackApplication`
4. Set **JRE:** Java 17

### VS Code

#### 1. Install Extensions

- **Extension Pack for Java** (Microsoft)
- **Spring Boot Extension Pack** (VMware)
- **Lombok Annotations Support**

#### 2. Configure Java

Create `.vscode/settings.json`:
```json
{
  "java.configuration.updateBuildConfiguration": "automatic",
  "java.jdt.ls.java.home": "/path/to/java-17",
  "spring-boot.ls.java.home": "/path/to/java-17"
}
```

#### 3. Run Configuration

Create `.vscode/launch.json`:
```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "MediTrack",
      "request": "launch",
      "mainClass": "com.airtribe.meditrack.MediTrackApplication",
      "projectName": "meditrack"
    }
  ]
}
```

---

## Environment Variables

### Setting Environment Variables

#### Windows (PowerShell)
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:MAVEN_HOME = "C:\Program Files\Apache\maven"
$env:PATH += ";$env:JAVA_HOME\bin;$env:MAVEN_HOME\bin"
```

#### Linux/Mac (Bash)
```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
export MAVEN_HOME=/usr/share/maven
export PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH
```

### Verify Environment Variables

```bash
echo $JAVA_HOME
echo $MAVEN_HOME
echo $PATH
```

---

## Next Steps

After successful setup:

1. ✅ Explore the **Swagger UI** to understand available APIs
2. ✅ Check the **H2 Console** to view database schema
3. ✅ Read the **[Design Decisions](Design_Decisions.md)** document
4. ✅ Review the **[API Documentation](../README.md#api-documentation)**
5. ✅ Run the **test suite** to verify all features
6. ✅ Start building your own features!

---

## Support

For issues or questions:

- **Email:** bgsudharma1998@airtribe.com
- **GitHub Issues:** [Create an issue](https://github.com/Sudharmabg/MediTrack/issues)
- **Documentation:** [README.md](../README.md)

---

**Happy Coding! 🚀**
