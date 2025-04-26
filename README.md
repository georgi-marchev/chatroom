# Online Chatroom

This project is created as an assignment for a Java Web Technologies university course.

The project is a backend server for a chatroom with frontend assets included as static resources.

The application consists of a **Java (Spring Boot)** backend with **Maven** for building and managing the project.

Client-server communication is implemented through WebSockets (Spring WebSocket).

## Setup and Usage

### Prerequisites

Before getting started, ensure that the following tools are installed:

- **Java >= 17+** (JDK) for backend development.

### Running Locally

1. Clone the repository:

   ```bash
   git clone https://github.com/georgi-marchev/chatroom.git
   cd chatroom
   ```

2. **Backend Setup** (Java + Maven)

- You can use the **Maven Wrapper** to build the project:

  ```bash
  ./mvnw clean install
  ```

- Alternatively, if Maven is installed globally, run:

  ```bash
  mvn clean install
  ```

- This command will **Package the backend** into a `.jar` file, which includes the frontend files in the `static` 
  directory.

3. Access the application at `http://localhost:8080/`. If the port is not available, add the following lines to the 
   configuration file with the desired port:
```yaml
server:
  port: 8080
```

### Configuration

1. **Application Configuration**:

    - In order to run the application, you need to configure the [application.yaml](./src/main/resources/application.yaml). This file contains settings for your database, email service (SMTP relay), and other application-specific settings.
