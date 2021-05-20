import avro.schema
from avro.datafile import DataFileReader, DataFileWriter
from avro.io import DatumReader, DatumWriter

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