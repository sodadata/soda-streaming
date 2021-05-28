
import time
import os
from utils.io import CustomAvroKafkaPublisher

import logging

# Set proper logging
logging.basicConfig(format="%(asctime)s | %(threadName)s | %(message)s",
                    level=logging.DEBUG,
                    datefmt="%H:%M:%S")


if __name__ == "__main__":
    expedia_kafka_config = {
        "bootstrap_server": os.getenv("BOOTSTRAP_SERVER", 'localhost:9092'),
        "topic_name": "stream1",
        "topic_description": "expedia"
    }
    expedia_data_generation_rate_msg_sec = int(os.getenv("EXPEDIA_RATE_MSG_SEC", 10))
    expedia_publisher = CustomAvroKafkaPublisher(kafka_config=expedia_kafka_config,
                                                 data_generation_rate_msg_sec=expedia_data_generation_rate_msg_sec)

    hellofresh_kafka_config = {
        "bootstrap_server": os.getenv("BOOTSTRAP_SERVER", 'localhost:9092'),
        "topic_name": "stream2",
        "topic_description": "hellofresh"
    }
    hellofresh_data_generation_rate_msg_sec = int(os.getenv("HELLOFRESH_RATE_MSG_SEC", 5))
    hellofresh_publisher = CustomAvroKafkaPublisher(kafka_config=hellofresh_kafka_config,
                                                    data_generation_rate_msg_sec=hellofresh_data_generation_rate_msg_sec)


    publishers = [expedia_publisher, hellofresh_publisher]
    for publisher in publishers:
        publisher.start()
        #publisher.join()



