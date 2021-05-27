from confluent_kafka import Producer
import time
import os
from utils.generator import generate_random_record
from utils.io import get_kafka_ready_avro_record
from utils.io import read_schema

bootstrap_server = os.getenv("BOOTSTRAP_SERVER", 'localhost:9092')
print("set bootstrap_server to %s" %bootstrap_server)
p = Producer({'bootstrap.servers': bootstrap_server})


def delivery_report(err, msg):
    """ Called once for each message produced to indicate delivery result.
        Triggered by poll() or flush(). """
    if err is not None:
        print('Message delivery failed: {}'.format(err))
    else:
        print('Message delivered to {} [{}]'.format(msg.topic(), msg.partition()))


def get_avro_data_from_file(path:str):
    #from fastavro import reader
    #avro_records = []
    with open(path, 'rb') as fo:
        b = fo.readlines()
        print(type(b))
    return b

schema = read_schema("./schemas/expedia.avsc")

for i in range(1000):
    time.sleep(2)
    random_data = generate_random_record()
    avro_serialized_data = get_kafka_ready_avro_record(schema, random_data)
    p.produce("stream1", avro_serialized_data, callback=delivery_report)
    print("produced new message")
p.flush()

