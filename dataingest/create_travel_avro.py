from utils.io import read_schema, create_avro_filewriter, show_content_avro_file
from utils.generator import generate_random_record

"""
FILL OUT THESE VARIABLES:
"""
number_of_records_to_randomly_create = 1000
show_content_stdout = True

if __name__ == "__main__":
    schema = read_schema("./schemas/travel.avsc")
    file_writer = create_avro_filewriter("./data/travel_test.avro", schema)
    for i in range(number_of_records_to_randomly_create):
        file_writer.append(generate_random_record())
    file_writer.close()

    if show_content_stdout:
        show_content_avro_file("./data/travel_test.avro")






