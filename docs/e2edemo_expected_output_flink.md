## Expected output logs from e2edemo.sh script (Flink job output)

```
taskmanager       |  ---- 
taskmanager       | Next output at: Thu Jun 03 09:10:40 UTC 2021
taskmanager       | 
taskmanager       | Scan summary --- 
taskmanager       |   | timestamp: Thu Jun 03 09:10:40 UTC 2021
taskmanager       |   | topic: hellofresh
taskmanager       |   | message-count: 50 
taskmanager       |   | null-count(boxPrice): 29 
taskmanager       |   | number-avg(boxPrice): 6.65 
taskmanager       |   | number-max(boxPrice): 9.70 
taskmanager       |   | number-min(boxPrice): 4.50 
taskmanager       |   | null-count(foodAllergiesDescription): 32 
taskmanager       |   | string-length-avg(foodAllergiesDescription): 97.56 
taskmanager       |   | string-length-max(foodAllergiesDescription): 188 
taskmanager       |   | string-length-min(foodAllergiesDescription): 17 
taskmanager       |   | categorical-frequency(menuPreferenceTypes): {Vegetarian=0.1200, QuickMeal=0.0800, Family=0.3400, Fish=0.2000, Vegan=0.1200, Meat=0.1400} 
taskmanager       |   | number-avg(numberOfMeals): 4.26 
taskmanager       |   | number-max(numberOfMeals): 5.00 
taskmanager       |   | number-min(numberOfMeals): 3.00 
taskmanager       |   | number-avg(numberOfPeople): 3.14 
taskmanager       |   | number-max(numberOfPeople): 6.00 
taskmanager       |   | number-min(numberOfPeople): 1.00 
taskmanager       |  ---- 
taskmanager       | Next output at: Thu Jun 03 09:10:50 UTC 2021
taskmanager       | 
taskmanager       | Scan summary --- 
taskmanager       |   | timestamp: Thu Jun 03 09:10:40 UTC 2021
taskmanager       |   | topic: expedia
taskmanager       |   | message-count: 99 
taskmanager       |   | number-avg(checkInDateDay): 15.56 
taskmanager       |   | number-max(checkInDateDay): 28.00 
taskmanager       |   | number-min(checkInDateDay): 1.00 
taskmanager       |   | number-avg(checkInDateMonth): 6.61 
taskmanager       |   | number-max(checkInDateMonth): 12.00 
taskmanager       |   | number-min(checkInDateMonth): 1.00 
taskmanager       |   | number-avg(checkInDateYear): 2010.32 
taskmanager       |   | number-max(checkInDateYear): 2021.00 
taskmanager       |   | number-min(checkInDateYear): 2000.00 
taskmanager       |   | null-count(destinationRegionId): 60 
taskmanager       |   | string-length-avg(destinationRegionId): 5.92 
taskmanager       |   | string-length-max(destinationRegionId): 6 
taskmanager       |   | string-length-min(destinationRegionId): 5 
taskmanager       |   | string-length-avg(destinationRegionName): 31.22 
taskmanager       |   | string-length-max(destinationRegionName): 64 
taskmanager       |   | string-length-min(destinationRegionName): 15 
taskmanager       |   | null-count(maxBudgetEuro): 75 
taskmanager       |   | number-avg(maxBudgetEuro): 587.50 
taskmanager       |   | number-max(maxBudgetEuro): 949.00 
taskmanager       |   | number-min(maxBudgetEuro): 195.00 
taskmanager       |   | categorical-frequency(propertyTypes): {Bed_Breakfast=0.1717, Villa=0.1313, Motel=0.1111, Condo=0.1515, Private_Vacation_Home=0.2020, Apartment=0.1111, Hotel=0.1212} 
taskmanager       |  ---- 
taskmanager       | Next output at: Thu Jun 03 09:10:50 UTC 2021
```
