# Smart Irrigation Management System

## Introduction
The Smart Irrigation Management System is an automated solution that integrates sensors, real-time monitoring, and intelligent control mechanisms to manage irrigation systems efficiently. The application uses Kafka for real-time event processing, PostgreSQL for data persistence, and includes a Python-based sensor simulation component. The system features WhatsApp integration for alerts and notifications.

The system utilizes three Kafka topics for communication:
- `irrigation_requests`: Handles incoming irrigation control commands
- `irrigation_responses`: Manages sensor feedback and status updates
- `alerts`: Manages system alerts and notifications

### Alert System Example
The system sends alerts through both Kafka and WhatsApp when issues are detected. Here's an example of the alert flow:

1. Kafka Alert Message:
![Kafka Alert Example](![alt text](image-1.png))
```json
{"plot_id": 3, "status": "failure", "message": "Sensor failed to irrigate the plot."}
```

2. WhatsApp Alert Notifications:
![WhatsApp Alert Example](![alt text](image.png))

These alerts are sent in real-time and can be monitored through both the Kafka topic and WhatsApp interface, ensuring immediate notification of any irrigation system issues.

## Prerequisites
- Java 21
- Apache Kafka
- PostgreSQL
- Python (for sensor simulation)
- Postman (for API testing)

## Setup Instructions

### 1. Kafka Setup
Navigate to the `setup-util` folder and run the following commands in separate terminals:

```bash
# Start Zookeeper
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties

# Start Kafka Server
.\bin\windows\kafka-server-start.bat .\config\server.properties
```

### 2. PostgreSQL Setup
1. Ensure PostgreSQL is running on the default port
2. Execute the `irrigation_db.sql` script located in the project root to set up the database schema

### 3. Sensor Simulation Setup
1. Navigate to the `sensor-simulation` folder
2. Install Python dependencies:
   ```bash
   pip install -r requirements.txt
   ```
3. Start the sensor simulator:
   ```bash
   python sensor_producer.py
   ```

#### Important Notes about Sensor Simulation:
- The simulator acts as both a producer and consumer:
  - Produces messages to topic: `irrigation_responses`
  - Consumes messages from topic: `irrigation_requests`
- Trigger events occur every 1 minute
- Plot ID 3 is configured to send WhatsApp alerts at specified times for demonstration purposes

### 4. API Testing
- Import the Postman collection from the `setup-util` folder to test the available endpoints

## System Architecture
The system operates with the following components:
- Spring Boot application for core business logic
- Kafka for event streaming
- PostgreSQL for data persistence
- Python-based sensor simulation
- WhatsApp integration for alerts

## Additional Information
- All configuration files use default ports and settings
- The system is designed to handle real-time irrigation events and alerts
- WhatsApp notifications are currently configured for demonstration purposes on specific plot IDs

## Troubleshooting
If you encounter any issues:
1. Ensure all services (Kafka, Zookeeper, PostgreSQL) are running
2. Check the logs for any error messages
3. Verify that all required dependencies are installed
4. Confirm that the database script has been executed successfully

## Support and Contact
For technical support or guidance during setup, please contact:
- Phone: 01141310002

Feel free to reach out if you need any assistance with the installation or configuration process.
