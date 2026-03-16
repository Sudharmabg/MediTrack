# JVM Architecture Report

## Table of Contents
1. [Introduction](#introduction)
2. [JVM Architecture Overview](#jvm-architecture-overview)
3. [Class Loader Subsystem](#class-loader-subsystem)
4. [Runtime Data Areas](#runtime-data-areas)
5. [Execution Engine](#execution-engine)
6. [JIT Compiler vs Interpreter](#jit-compiler-vs-interpreter)
7. [Write Once, Run Anywhere (WORA)](#write-once-run-anywhere-wora)
8. [Garbage Collection](#garbage-collection)
9. [JVM in MediTrack](#jvm-in-meditrack)

---

## Introduction

The **Java Virtual Machine (JVM)** is an abstract computing machine that enables a computer to run Java programs. It is the cornerstone of Java's platform independence and provides a runtime environment for executing Java bytecode.

### Key Components
- **JDK (Java Development Kit)**: Complete development environment (compiler, debugger, JRE)
- **JRE (Java Runtime Environment)**: Runtime environment (JVM + libraries)
- **JVM (Java Virtual Machine)**: Executes bytecode

```
┌─────────────────────────────────────┐
│         JDK (Development)           │
│  ┌───────────────────────────────┐  │
│  │     JRE (Runtime)             │  │
│  │  ┌─────────────────────────┐  │  │
│  │  │   JVM (Execution)       │  │  │
│  │  │                         │  │  │
│  │  └─────────────────────────┘  │  │
│  │                               │  │
│  └───────────────────────────────┘  │
│                                     │
└─────────────────────────────────────┘
```

---

## JVM Architecture Overview

The JVM architecture consists of three main subsystems:

```
┌────────────────────────────────────────────────────────────┐
│                     JVM ARCHITECTURE                       │
├────────────────────────────────────────────────────────────┤
│                                                            │
│  ┌──────────────────────────────────────────────────────┐ │
│  │          1. CLASS LOADER SUBSYSTEM                   │ │
│  │  ┌────────────┐  ┌────────────┐  ┌────────────┐     │ │
│  │  │ Bootstrap  │→ │ Extension  │→ │ Application│     │ │
│  │  │  Loader    │  │   Loader   │  │   Loader   │     │ │
│  │  └────────────┘  └────────────┘  └────────────┘     │ │
│  └──────────────────────────────────────────────────────┘ │
│                           ↓                                │
│  ┌──────────────────────────────────────────────────────┐ │
│  │          2. RUNTIME DATA AREAS (Memory)              │ │
│  │  ┌─────────┐  ┌──────┐  ┌────────┐  ┌──────────┐   │ │
│  │  │ Method  │  │ Heap │  │ Stack  │  │ PC Reg   │   │ │
│  │  │  Area   │  │      │  │        │  │ Native   │   │ │
│  │  └─────────┘  └──────┘  └────────┘  └──────────┘   │ │
│  └──────────────────────────────────────────────────────┘ │
│                           ↓                                │
│  ┌──────────────────────────────────────────────────────┐ │
│  │          3. EXECUTION ENGINE                         │ │
│  │  ┌────────────┐  ┌────────────┐  ┌────────────┐     │ │
│  │  │Interpreter │  │ JIT        │  │ Garbage    │     │ │
│  │  │            │  │ Compiler   │  │ Collector  │     │ │
│  │  └────────────┘  └────────────┘  └────────────┘     │ │
│  └──────────────────────────────────────────────────────┘ │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

---

## Class Loader Subsystem

The **Class Loader** is responsible for loading `.class` files into memory. It performs three main activities:

### 1. Loading
Reads `.class` files and generates binary data in the Method Area.

**Three Types of Class Loaders:**

```
┌─────────────────────────────────────┐
│     Bootstrap Class Loader          │  ← Loads core Java classes
│     (rt.jar, i18n.jar)              │     (String, Object, etc.)
└─────────────────────────────────────┘
              ↓ Parent
┌─────────────────────────────────────┐
│     Extension Class Loader          │  ← Loads extension classes
│     (jre/lib/ext)                   │     (security, crypto)
└─────────────────────────────────────┘
              ↓ Parent
┌─────────────────────────────────────┐
│     Application Class Loader        │  ← Loads application classes
│     (CLASSPATH)                     │     (our MediTrack code)
└─────────────────────────────────────┘
```

**Example in MediTrack:**
```java
// Application Class Loader loads our classes
com.airtribe.meditrack.entity.Doctor
com.airtribe.meditrack.service.DoctorService

// Bootstrap Class Loader loads
java.lang.String
java.util.ArrayList

// Extension Class Loader loads
javax.crypto.Cipher
```

### 2. Linking
- **Verification**: Ensures bytecode is valid and secure
- **Preparation**: Allocates memory for static variables
- **Resolution**: Replaces symbolic references with direct references

### 3. Initialization
- Executes static blocks and initializes static variables
- Happens in order of class hierarchy (parent first)

**Example in MediTrack:**
```java
public class Constants {
    // Preparation: memory allocated
    public static final double TAX_RATE;
    
    // Initialization: static block executed
    static {
        TAX_RATE = 0.18;  // Initialized here
        System.out.println("Constants class initialized");
    }
}
```

---

## Runtime Data Areas

The JVM divides memory into several runtime data areas:

### 1. **Method Area** (Shared across all threads)
- Stores class-level data:
  - Class metadata (name, parent, methods, fields)
  - Static variables
  - Constant pool
  - Method bytecode

**Example:**
```java
public class Doctor extends Person {
    private static int doctorCount = 0;  // Stored in Method Area
    private String name;                  // Metadata in Method Area
}
```

### 2. **Heap** (Shared across all threads)
- Stores all **objects** and **instance variables**
- Managed by **Garbage Collector**
- Divided into generations:
  - **Young Generation** (Eden, Survivor spaces)
  - **Old Generation** (Tenured)
  - **Permanent Generation** (Metaspace in Java 8+)

**Example:**
```java
Doctor doctor = new Doctor();  // Object created in Heap
Patient patient = new Patient(); // Object created in Heap
```

```
HEAP STRUCTURE:
┌────────────────────────────────────────┐
│         YOUNG GENERATION               │
│  ┌──────┐  ┌─────────┐  ┌─────────┐  │
│  │ Eden │  │Survivor │  │Survivor │  │
│  │      │  │   S0    │  │   S1    │  │
│  └──────┘  └─────────┘  └─────────┘  │
├────────────────────────────────────────┤
│         OLD GENERATION (Tenured)       │
│  Long-lived objects                    │
├────────────────────────────────────────┤
│         METASPACE (Java 8+)            │
│  Class metadata                        │
└────────────────────────────────────────┘
```

### 3. **Stack** (One per thread)
- Stores **local variables** and **method call frames**
- Each method call creates a **Stack Frame**:
  - Local variables
  - Operand stack
  - Frame data (return address)

**Example:**
```java
public void createAppointment(Long doctorId, Long patientId) {
    // doctorId, patientId stored in Stack Frame
    Doctor doctor = findDoctor(doctorId);  // 'doctor' reference in Stack
    // Actual Doctor object is in Heap
}
```

```
STACK STRUCTURE (per thread):
┌─────────────────────────┐
│   Stack Frame 3         │ ← Current method
│   createAppointment()   │
│   - doctorId: 1         │
│   - patientId: 5        │
├─────────────────────────┤
│   Stack Frame 2         │
│   processRequest()      │
├─────────────────────────┤
│   Stack Frame 1         │
│   main()                │
└─────────────────────────┘
```

### 4. **Program Counter (PC) Register** (One per thread)
- Holds the address of the **currently executing instruction**
- Updated after each instruction

### 5. **Native Method Stack** (One per thread)
- Stores native method information (C/C++ code)
- Used by JNI (Java Native Interface)

---

## Execution Engine

The **Execution Engine** executes the bytecode. It has three main components:

### 1. **Interpreter**
- Reads bytecode line-by-line
- Executes instructions one at a time
- **Fast startup**, but **slow execution**

### 2. **JIT Compiler (Just-In-Time)**
- Compiles frequently executed bytecode (hot spots) into **native machine code**
- **Slow startup**, but **fast execution**
- Optimizations:
  - Inlining
  - Dead code elimination
  - Loop optimization

### 3. **Garbage Collector**
- Automatically reclaims memory from unused objects
- Algorithms:
  - Serial GC
  - Parallel GC
  - CMS (Concurrent Mark-Sweep)
  - G1 GC (Garbage First)
  - ZGC (Low-latency)

---

## JIT Compiler vs Interpreter

| Aspect | Interpreter | JIT Compiler |
|--------|-------------|--------------|
| **Execution** | Line-by-line | Compiles to native code |
| **Startup Time** | Fast | Slow |
| **Runtime Performance** | Slow | Fast |
| **Memory Usage** | Low | High |
| **Use Case** | Initial execution | Hot code paths |

### How They Work Together

```
┌────────────────────────────────────────────────────┐
│                  BYTECODE                          │
└────────────────────────────────────────────────────┘
                    ↓
        ┌───────────┴───────────┐
        ↓                       ↓
┌───────────────┐      ┌────────────────┐
│  INTERPRETER  │      │  JIT COMPILER  │
│  (Cold code)  │      │  (Hot code)    │
└───────────────┘      └────────────────┘
        ↓                       ↓
┌───────────────┐      ┌────────────────┐
│   Execute     │      │ Native Machine │
│  Immediately  │      │     Code       │
└───────────────┘      └────────────────┘
                              ↓
                    ┌────────────────┐
                    │  Fast Execution│
                    └────────────────┘
```

**Example:**
```java
// First few calls: Interpreted
for (int i = 0; i < 10000; i++) {
    calculateBill();  // After ~1000 calls, JIT compiles this
}
// Subsequent calls: Native code (much faster)
```

---

## Write Once, Run Anywhere (WORA)

Java's platform independence is achieved through:

### 1. **Bytecode**
- Java source code (`.java`) is compiled to **platform-independent bytecode** (`.class`)
- Bytecode is the same on all platforms

### 2. **JVM**
- Each platform has its own **JVM implementation**
- JVM translates bytecode to **platform-specific machine code**

```
┌─────────────────────────────────────────────────────┐
│         WRITE ONCE, RUN ANYWHERE                    │
└─────────────────────────────────────────────────────┘

    MediTrack.java (Source Code)
            ↓
      javac (Compiler)
            ↓
    MediTrack.class (Bytecode) ← Platform Independent
            ↓
    ┌───────┴───────┬───────────┬───────────┐
    ↓               ↓           ↓           ↓
┌────────┐    ┌────────┐  ┌────────┐  ┌────────┐
│Windows │    │ Linux  │  │  macOS │  │Embedded│
│  JVM   │    │  JVM   │  │  JVM   │  │  JVM   │
└────────┘    └────────┘  └────────┘  └────────┘
    ↓               ↓           ↓           ↓
┌────────┐    ┌────────┐  ┌────────┐  ┌────────┐
│Windows │    │ Linux  │  │  macOS │  │  ARM   │
│Machine │    │Machine │  │Machine │  │Machine │
│  Code  │    │  Code  │  │  Code  │  │  Code  │
└────────┘    └────────┘  └────────┘  └────────┘
```

**MediTrack Example:**
```bash
# Compile once
javac MediTrackApplication.java

# Run anywhere
java MediTrackApplication  # Windows
java MediTrackApplication  # Linux
java MediTrackApplication  # macOS
```

---

## Garbage Collection

### How GC Works

1. **Mark**: Identify live objects (reachable from GC roots)
2. **Sweep**: Remove dead objects
3. **Compact**: Defragment memory (optional)

### GC Roots
- Local variables in Stack
- Static variables in Method Area
- Active threads
- JNI references

**Example in MediTrack:**
```java
public void processAppointment() {
    Doctor doctor = new Doctor();  // GC Root (local variable)
    doctor.setName("Dr. Smith");
    
    // After method returns, 'doctor' is no longer a GC root
    // Object becomes eligible for GC if no other references exist
}
```

### Generational GC

```
┌────────────────────────────────────────┐
│  NEW OBJECTS → Young Generation        │
│  (Minor GC - frequent, fast)           │
│                                        │
│  If survives multiple GCs ↓            │
│                                        │
│  LONG-LIVED → Old Generation           │
│  (Major GC - infrequent, slow)         │
└────────────────────────────────────────┘
```

---

## JVM in MediTrack

### Memory Usage in MediTrack

```java
// Method Area: Class metadata
class Doctor extends Person {
    private static int count = 0;  // Method Area
}

// Heap: Objects
Doctor doctor = new Doctor();  // Heap
Patient patient = new Patient();  // Heap
List<Appointment> appointments = new ArrayList<>();  // Heap

// Stack: Local variables
public void createAppointment(Long id) {
    // 'id' in Stack
    Appointment apt = findAppointment(id);  // 'apt' reference in Stack
}
```

### JVM Tuning for MediTrack

```properties
# application.properties
# Heap size
-Xms512m  # Initial heap size
-Xmx2g    # Maximum heap size

# GC selection
-XX:+UseG1GC  # Use G1 Garbage Collector

# GC logging
-Xlog:gc*:file=gc.log
```

### Monitoring JVM

```bash
# Check JVM version
java -version

# Monitor JVM
jconsole  # GUI monitoring tool
jvisualvm # Advanced profiling

# Heap dump
jmap -dump:format=b,file=heap.bin <pid>
```

---

## Summary

### Key Takeaways

1. **JVM Architecture**:
   - Class Loader → Runtime Data Areas → Execution Engine

2. **Memory Areas**:
   - **Method Area**: Class metadata, static variables
   - **Heap**: Objects
   - **Stack**: Local variables, method frames
   - **PC Register**: Current instruction
   - **Native Stack**: Native methods

3. **Execution**:
   - **Interpreter**: Fast startup, slow execution
   - **JIT**: Slow startup, fast execution (hot code)

4. **Platform Independence**:
   - Java → Bytecode (platform-independent)
   - JVM → Native code (platform-specific)

5. **Garbage Collection**:
   - Automatic memory management
   - Generational approach (Young → Old)

---


