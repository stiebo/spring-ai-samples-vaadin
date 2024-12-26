# Spring AI Samples with Vaadin

This repository demonstrates the integration of AI and machine learning features with Vaadin and Spring Boot. It serves as a sample application showcasing the capabilities of modern AI-powered solutions within a Vaadin framework.

## Features

- **Vaadin Integration**
- **Spring Boot Backend**
- **Spring AI**
- **Postgres/PGVector Vectorstore via Docker**
- **Modular Design**


## How to Build and Run

### Running the Application

1. Start the Spring Boot application:
   ```bash
   ./mvnw clean spring-boot:run
   ```
   
2. Open your browser and navigate to:
   ```
   http://localhost:8080
   ```

### Running in Production Mode

1. Build the project with the production profile:
   ```bash
   ./mvnw clean package -Pproduction
   ```

### Docker Support

To run the application in a Docker container:

1. Build the Docker image:
   ```bash
   docker build -t spring-ai-samples-vaadin .
   ```

2. Run the container:
   ```bash
   docker run -p 8080:8080 spring-ai-samples-vaadin
   ```

3. Access the application at:
   ```
   http://localhost:8080
   ```
   
4. Optional:
use Docker and compose.prod.yaml (rename to compose.yaml) to run as standalone app
provide OpenAI API key, PGVector DB username and password via .env or environment variables

