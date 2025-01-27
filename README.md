# IoT Irrigation System

This project is an IoT-based irrigation system built with Spring Boot and Kafka integration. The system is designed to manage and automate the irrigation process for agricultural plots.

## Features

- **Project Structure**: Basic project structure set up with Spring Boot.
- **Kafka Integration**: Configured Kafka for communication between services.
- **Entities**: Implemented `Plot` and `TimeSlot` entities with JPA repositories.
- **CRUD Operations**: Added `PlotController` and `PlotService` with CRUD operations.
- **DTOs**: Created Data Transfer Objects (DTOs) for data transfer.
- **Configuration**: Configured application properties for PostgreSQL and Kafka.
- **Exception Handling**: Added global exception handling.
- **Irrigation Scheduling**: Implemented irrigation scheduling and producer service.

## Getting Started

### Prerequisites

- Java 11 or higher
- Apache Kafka
- PostgreSQL

### Installation

1. Clone the repository:
   ```sh
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```sh
   cd <project-directory>
   ```
3. Set up PostgreSQL and Kafka configurations in `application.properties`.

### Running the Application

1. Start the PostgreSQL and Kafka services.
2. Build and run the application:
   ```sh
   ./mvnw spring-boot:run
   ```

## Usage

- Use the `PlotController` to manage plots and their irrigation schedules.
- Kafka is used for communication between different services in the system.

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

## License

This project is licensed under the MIT License.

## Acknowledgements

- Spring Boot
- Apache Kafka
- PostgreSQL


## New Features

- **Push Protection**: Implemented GitHub push protection to prevent secrets from being pushed to the repository. This includes scanning for sensitive information such as Twilio Account String Identifiers and blocking the push if any secrets are detected.
- **Sensor Simulation**: Added a sensor simulation feature to simulate the behavior of sensors in the irrigation system. This includes a 70% chance of success and a 30% chance of failure for sensor interactions.
- **Kafka Integration**: Enhanced Kafka integration for better communication between different services. This includes setting up Kafka producers and consumers for handling irrigation requests and responses.
- **Logging**: Configured logging to provide detailed information about the system's operations, including sensor interactions and Kafka message processing.

