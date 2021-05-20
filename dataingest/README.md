# Expedia avro schema creation

## Project structure:
```
├── data
│   ├── expedia_test.avro
│   └── final_expedia.json
├── expedia.avro
├── main_expedia_avro.py
├── README.md
├── requirements.txt
├── schemas
│   └── expedia.avsc
└── utils
    ├── generator.py
    ├── __init__.py
    ├── io.py
    └── __pycache__
```

## Run project
1. Create virtualenv
2. Install python requirements through `pip install -r requirements.txt`
3. Run main_expedia_avro.py after setting the required variables:
   1. *numbers_of_records_to_randomly_create*
   2. *show_content_stdout*


## Expedia data example
- As found during Hotel booking searches -> web developer -> json structure
- A short version of this json has been used to create the `expedia_test.avro`
file. Since the original json was heavily nested, some relevant fields were
chosen and the datastructure was flattened.
- Example json (you can see this pretty print structure through `jq '.' /data/final_expedia.json`)
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