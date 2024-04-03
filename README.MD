# CDX Fromtis WS

## Overview

This Spring Boot project, **cdx-fromtis-ws**, serves a SOAP endpoint for digital certification requests, leveraging the robust framework of Spring Boot for efficient development and deployment.

## Features

- **SOAP Endpoint:** A dedicated service for processing digital certification requests.
- **Actuator Health Check:** An endpoint to monitor the application's health, ensuring reliability and availability.

## Prerequisites

To use or contribute to this project, you need to have the following installed on your system:
- JDK 21 or newer
- Maven 3.2 or above

## Setting Up

Follow these steps to get the project up and running on your local machine:

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd cdx-fromtis-ws
   ```  
Replace <repository-url> with the URL of this project's repository.

2. **Build the project with Maven:**
    ```bash
    mvn clean install
    ```

3. **Run the application:**

    ```bash
    mvn spring-boot:run
    ```

This will launch the application on the default port (8080). You can modify the port by altering the application.properties file or by appending **--server.port=<port>** to the Maven command.

## Endpoints

The application exposes two main endpoints:

- SOAP Endpoint for Digital Certification Requests:

```
http://localhost:8080/fromtis/ws/requisicaoCertificacaoDigital
```

Replace localhost and 8080 with the appropriate host and port as necessary.

**Health Check Endpoint:**

```
http://localhost:8080/actuator/health
```

This endpoint is useful for monitoring the health status of the service.

## Usage

To interact with the SOAP endpoint, tools such as SoapUI or Postman configured for SOAP can be used. Import the WSDL from the project's /src/main/resources/wsdl/ directory to these tools and proceed with the request generation towards the service.

## Docker Deployment

This project can be easily containerized and deployed using Docker, thanks to the provided Docker Compose configuration and custom entrypoint script. Follow the steps below to deploy your application using Docker.

### Prerequisites

- Docker and Docker Compose installed on your machine.

### Docker Compose Configuration

The Docker Compose configuration is located under `src/main/docker/app.yml`. This configuration sets up the application with necessary environment variables, port mappings, and health checks for production deployment:

- **Image Name:** `fromtisws`
- **Environment Variables:** Configure Java options and Spring profiles for optimal performance.
- **Ports:** Binds port 8080 on localhost to the same port in the container for accessing the application.
- **Health Check:** Utilizes `curl` to check the application's health at `/actuator/health`.

### Custom Entrypoint Script

A custom entrypoint script, `entrypoint.sh`, is located under `src/main/docker/jib`. This script adds a delay before starting the application, which can be useful in environments where services need to start up in a specific order. It also sets Java options for the application startup.

### Steps to Deploy with Docker

1. **Build your Docker image:**

   Navigate to the root directory of the project and build the Docker image with:

   ```bash
   docker build -t fromtisws .
    ```

2. **Run with Docker Compose:**

    From the same directory where your app.yml file is located, run:

    ```bash
    docker-compose -f src/main/docker/app.yml up
    ```

This command starts the application as defined in the Docker Compose configuration. It respects the environment variables, port mappings, and health checks.

3. **Accessing the Application**

After deployment, the application is accessible at http://localhost:8080. The actuator endpoint for health checks can be accessed at http://localhost:8080/actuator/health.