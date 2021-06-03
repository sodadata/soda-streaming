# Custom data generator

## Project structure:
```
├── create_travel_avro.py
├── data
│   └── final_travel.json
├── Dockerfile
├── main_pub_kafka.py
├── README.md
├── requirements.txt
├── schemas
│   ├── travel.avsc
│   └── food.avsc
├── test
│   └── validate_avro_format_from_kafka.py
└── utils
    ├── generator.py
    ├── __init__.py
    ├── io.py
    ├── pubsub

```

## Run project
1. Create virtualenv
2. Install python requirements through `pip install -r requirements.txt`
3. Run main_pub_kafka.py after specifying the necessary details for the
*CustomAvroKafkaPublisher*.
4. To change the data generation rate for the different customers, set
the env-vars to a value of your choice; env-var has the following format:
```<CUSTOMER_NAME>_RATE_MSG_SEC```



## Travel data example
- As found during Hotel booking searches -> web developer -> json structure
- A short version of this json has been used to create the `travel_test.avro`
file. Since the original json was heavily nested, some relevant fields were
chosen and the datastructure was flattened.
- Example json (you can see this pretty print structure through `jq '.' /data/final_travel.json`)
```
{
  "checkInDateYear": 2014,
  "checkInDateMonth": 12,
  "checkInDateDay": 7,
  "destinationRegionId": "486714",
  "destinationRegionName": "ANTWERP- downtown",
  "maxBudgetEuro": 753.7999877929688,
  "propertyTypes": "Condo"
}
```



## Details on AVRO:
https://docs.oracle.com/database/nosql-12.1.3.0/GettingStartedGuide/avroschemas.html