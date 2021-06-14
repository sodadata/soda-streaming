from queue import Queue
import time
import os
from utils.io import CustomAvroKafkaPublisher
from utils.configurator import RestConfigurator

import logging

# Set proper logging
LOGLEVEL = os.environ.get('LOGLEVEL', 'DEBUG').upper()
logging.basicConfig(format="%(asctime)s | %(levelname)s | %(threadName)s | %(message)s",
                    level=LOGLEVEL,
                    datefmt="%H:%M:%S")


if __name__ == "__main__":
    #todo: create the customavrokafkapublisher automatically and add queue
    all_queues = {"travel": Queue(), "food": Queue()}

    travel_kafka_config = {
        "bootstrap_server": os.getenv("BOOTSTRAP_SERVER", 'localhost:9092'),
        "topic_name": "travel",
        "topic_description": "travel"
    }
    travel_data_generation_rate_msg_sec = int(os.getenv("TRAVEL_RATE_MSG_SEC", 10))
    travel_publisher = CustomAvroKafkaPublisher(kafka_config=travel_kafka_config,
                                                 data_generation_rate_msg_sec=travel_data_generation_rate_msg_sec,
                                                 queue=all_queues["travel"])

    food_kafka_config = {
        "bootstrap_server": os.getenv("BOOTSTRAP_SERVER", 'localhost:9092'),
        "topic_name": "food",
        "topic_description": "food"
    }
    food_data_generation_rate_msg_sec = int(os.getenv("FOOD_RATE_MSG_SEC", 5))
    food_publisher = CustomAvroKafkaPublisher(kafka_config=food_kafka_config,
                                                    data_generation_rate_msg_sec=food_data_generation_rate_msg_sec,
                                                    queue=all_queues["food"])

    restconfigthread = RestConfigurator(all_queues=all_queues)
    restconfigthread.start()

    publishers = [travel_publisher, food_publisher]
    for publisher in publishers:
        publisher.start()
        #publisher.join()





