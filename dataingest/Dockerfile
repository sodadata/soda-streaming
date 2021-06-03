FROM python:3.8

ADD requirements.txt requirements.txt

RUN pip install -r requirements.txt

ADD data data
ADD schemas schemas
ADD utils utils
ADD main_pub_kafka.py main_pub_kafka.py

CMD ["python", "main_pub_kafka.py"]