FROM python:3.8

ADD requirements.txt requirements.txt

ADD data data
ADD schemas schemas
ADD utils utils
ADD main_pub_kafka.py main_pub_kafka.py

RUN pip install -r requirements.txt

CMD ["python", "main_pub_kafka.py"]