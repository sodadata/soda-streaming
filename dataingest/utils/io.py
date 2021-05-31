import avro.schema
from avro.datafile import DataFileReader, DataFileWriter
from avro.io import DatumReader, DatumWriter
import io
from typing import Dict
import threading
from confluent_kafka import Producer
import logging
from utils.generator import generate_random_record
import time

logger = logging.getLogger(__name__)

def read_schema(path: str):
    return avro.schema.parse(open(path).read())

def create_avro_filewriter(file_result_path:str, schema):
    return DataFileWriter(open(file_result_path, "wb"), DatumWriter(), schema)

def show_content_avro_file(file_result_path:str):
    with DataFileReader(open(file_result_path, "rb"), DatumReader()) as avro_file:
        records_seen = 0
        for record in avro_file:
            print("%d - %s" % (records_seen, record))
            records_seen += 1

def get_avro_data_from_file(path:str):
    #from fastavro import reader
    #avro_records = []
    with open(path, 'rb') as fo:
        b = fo.readlines()
        print(type(b))
    return b

def get_kafka_ready_avro_record(schema, random_record):
    writer = avro.io.DatumWriter(schema)
    bytes_writer = io.BytesIO()
    encoder = avro.io.BinaryEncoder(bytes_writer)
    writer.write(random_record, encoder)
    raw_bytes = bytes_writer.getvalue()
    return raw_bytes

class CustomAvroKafkaPublisher(threading.Thread):
    """
    Generalized avropublisher for random data
    """
    def __init__(self,
                 kafka_config: Dict[str, str],
                 data_generation_rate_msg_sec: float):
        threading.Thread.__init__(self, name=f"thread-{kafka_config['topic_description']}")
        self.bootstrap_server = kafka_config["bootstrap_server"]
        self.topic_name = kafka_config["topic_name"]
        self.topic_description = kafka_config["topic_description"]
        self.data_generation_rate_msg_sec = data_generation_rate_msg_sec
        self.schema = read_schema(f"./schemas/{self.topic_description}.avsc")

    def run(self):
        p = Producer({'bootstrap.servers': self.bootstrap_server})
        logger.info(f"Starting producer for {self.topic_name} with data rate of {self.data_generation_rate_msg_sec} msg/sec")
        counter_messages_published = 0
        while True:
            for i in range(10):
                random_data = generate_random_record(self.topic_description)
                avro_serialized_data = get_kafka_ready_avro_record(self.schema, random_data)
                p.produce(self.topic_name, avro_serialized_data, callback=delivery_report)
                counter_messages_published +=1
                logger.debug(f"Published to {self.topic_name} | totalling to {counter_messages_published} messages")
                time.sleep(1/self.data_generation_rate_msg_sec)
            p.flush()







def delivery_report(err, msg):
    """ Called once for each message produced to indicate delivery result.
        Triggered by poll() or flush(). """
    if err is not None:
        logger.debug('Message delivery failed: {}'.format(err))
    else:
        logger.debug('Message delivered to {} [{}]'.format(msg.topic(), msg.partition()))


