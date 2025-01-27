# from kafka import KafkaConsumer, KafkaProducer
# import json
# import time
# import logging

# # Configure logging
# logging.basicConfig(level=logging.INFO)
# logger = logging.getLogger(__name__)

# # Kafka Configuration
# KAFKA_BROKER = 'localhost:9092'
# IRRIGATION_REQUEST_TOPIC = 'irrigation_requests'
# IRRIGATION_RESPONSE_TOPIC = 'irrigation_responses'
# ALERTS_TOPIC = 'alerts'

# # Kafka Producer
# producer = KafkaProducer(
#     bootstrap_servers=KAFKA_BROKER,
#     value_serializer=lambda v: json.dumps(v).encode('utf-8')
# )

# # Kafka Consumer
# consumer = KafkaConsumer(
#     IRRIGATION_REQUEST_TOPIC,
#     bootstrap_servers=KAFKA_BROKER,
#     value_deserializer=lambda v: json.loads(v.decode('utf-8')),
#     group_id="sensor_group",
#     auto_offset_reset='earliest'
# )

# def simulate_sensor_interaction(plot_id, water_amount):
#     """
#     Simulate the sensor behavior: 70% chance of success, 30% chance of failure.
#     """
#     logger.info(f"Simulating sensor interaction for Plot {plot_id} with {water_amount} liters...")
#     time.sleep(2)  # Simulate sensor processing delay

#     if plot_id % 3 == 0:  # Simulate failure for some plots
#         return {"status": "failure", "message": "Sensor failed to irrigate the plot."}
#     return {"status": "success", "message": "Irrigation successful."}

# def process_requests():
#     """
#     Consume requests from the irrigation_requests topic and process them.
#     """
#     try:
#         for message in consumer:
#             request_data = message.value
#             plot_id = request_data['plot_id']
#             water_amount = request_data['water_amount']

#             logger.info(f"Received irrigation request for Plot {plot_id}: {request_data}")

#             # Simulate sensor interaction
#             result = simulate_sensor_interaction(plot_id, water_amount)

#             if result['status'] == 'success':
#                 # Send success response
#                 response_message = {
#                     "plot_id": plot_id,
#                     "status": "success",
#                     "message": result['message']
#                 }
#                 producer.send(IRRIGATION_RESPONSE_TOPIC, response_message)
#                 producer.flush()
#                 logger.info(f"Success response sent for Plot {plot_id}: {response_message}")
#             else:
#                 # Send failure alert
#                 alert_message = {
#                     "plot_id": plot_id,
#                     "status": "failure",
#                     "message": result['message']
#                 }
#                 producer.send(ALERTS_TOPIC, alert_message)
#                 producer.flush()
#                 logger.info(f"Failure alert sent for Plot {plot_id}: {alert_message}")
#     except Exception as e:
#         logger.error(f"Error in processing requests: {e}")

# if __name__ == "__main__":
#     logger.info("Sensor Service is running...")
#     process_requests()
from kafka import KafkaConsumer, KafkaProducer
import json
import time
import logging
import re

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Kafka Configuration
KAFKA_BROKER = 'localhost:9092'
IRRIGATION_REQUEST_TOPIC = 'irrigation_requests'
IRRIGATION_RESPONSE_TOPIC = 'irrigation_responses'
ALERTS_TOPIC = 'alerts'

# Kafka Producer
producer = KafkaProducer(
    bootstrap_servers=KAFKA_BROKER,
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)

# Kafka Consumer
consumer = KafkaConsumer(
    IRRIGATION_REQUEST_TOPIC,
    bootstrap_servers=KAFKA_BROKER,
    value_deserializer=lambda v: v.decode('utf-8'),  # Raw data to handle non-JSON
    group_id="sensor_group",
    auto_offset_reset='earliest'
)

def parse_raw_message(raw_message):
    """
    Parse raw non-JSON message into a valid JSON format.
    Expected format: "Plot ID: 2, Start Time: 04:53, End Time: 06:30, Water Volume: 500.00"
    """
    try:
        # Use regex to extract fields
        pattern = r"Plot ID: (\d+), Start Time: ([\d:]+), End Time: ([\d:]+), Water Volume: ([\d.]+)"
        match = re.match(pattern, raw_message)

        if match:
            plot_id, start_time, end_time, water_volume = match.groups()
            return {
                "plot_id": int(plot_id),
                "start_time": start_time,
                "end_time": end_time,
                "water_volume": float(water_volume)
            }
        else:
            logger.error(f"Failed to parse message: {raw_message}")
            return None
    except Exception as e:
        logger.error(f"Error while parsing raw message: {raw_message}, Error: {e}")
        return None

def simulate_sensor_interaction(plot_id, water_amount):
    """
    Simulate the sensor behavior: 70% chance of success, 30% chance of failure.
    """
    logger.info(f"Simulating sensor interaction for Plot {plot_id} with {water_amount} liters...")
    time.sleep(2)  # Simulate sensor processing delay

    if plot_id % 3 == 0:  # Simulate failure for some plots
        return {"status": "failure", "message": "Sensor failed to irrigate the plot."}
    return {"status": "success", "message": "Irrigation successful."}

def process_requests():
    """
    Consume requests from the irrigation_requests topic and process them.
    """
    try:
        for message in consumer:
            raw_message = message.value
            logger.info(f"Received raw message: {raw_message}")

            # Parse the raw message into JSON
            request_data = parse_raw_message(raw_message)
            if not request_data:
                logger.error(f"Skipping invalid message: {raw_message}")
                continue  # Skip invalid messages

            # Extract required fields
            plot_id = request_data['plot_id']
            water_amount = request_data['water_volume']

            logger.info(f"Parsed irrigation request for Plot {plot_id}: {request_data}")

            # Simulate sensor interaction
            result = simulate_sensor_interaction(plot_id, water_amount)

            if result['status'] == 'success':
                # Send success response
                response_message = {
                    "plot_id": plot_id,
                    "status": "success",
                    "message": result['message']
                }
                producer.send(IRRIGATION_RESPONSE_TOPIC, response_message)
                producer.flush()
                logger.info(f"Success response sent for Plot {plot_id}: {response_message}")
            else:
                # Send failure alert
                alert_message = {
                    "plot_id": plot_id,
                    "status": "failure",
                    "message": result['message']
                }
                producer.send(ALERTS_TOPIC, alert_message)
                producer.flush()
                logger.info(f"Failure alert sent for Plot {plot_id}: {alert_message}")
    except Exception as e:
        logger.error(f"Error in processing requests: {e}")

if __name__ == "__main__":
    logger.info("Sensor Service is running...")
    process_requests()
