from confluent_kafka import Producer
import time


p = Producer({'bootstrap.servers': 'localhost:9092'})

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


some_data_source = get_avro_data_from_file("./data/expedia_test.avro")


for avro_record in some_data_source:
    # Trigger any available delivery report callbacks from previous produce() calls
    p.poll(0)
    print(type(avro_record))
    print(avro_record, flush=True)
    # Asynchronously produce a message, the delivery report callback
    # will be triggered from poll() above, or flush() below, when the message has
    # been successfully delivered or failed permanently.
    p.produce('stream1', avro_record, callback=delivery_report)

    time.sleep(5)

# Wait for any outstanding messages to be delivered and delivery report
# callbacks to be triggered.

p.flush()