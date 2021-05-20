import random

def generate_random_record():
    return {"checkInDateYear": random.randint(2000, 2021),
     "checkInDateMonth": random.randint(1, 12),
     "checkInDateDay": random.randint(1, 28),
     "destinationRegionId": str(random.randint(50000, 700000)),
     "destinationRegionName": get_random_destinationregionname(),
     "maxBudgetEuro": random.randint(100, 10000)/10,
     "propertyTypes": choose_specific_propertytype()}


def get_random_destinationregionname():
    CHOICES = ["GHENT", "BRUSSELS", "ANTWERP", "BRUGES", "HASSELT", "OSTEND"]
    APPEND_CHOICES = ["- downtown", "> 1 km from citycentre", " (and vicinity)", " (and vicinity with limited access to public transport) "]
    return choose_random_from_list(CHOICES) + choose_random_from_list(APPEND_CHOICES)


def choose_specific_propertytype():
    CHOICES = ["Hotel", "Villa", "Apartment", "Condo", "Motel", "Bed_Breakfast", "Private_Vacation_Home"]
    return choose_random_from_list(CHOICES)


def choose_random_from_list(specific_list):
    return specific_list[random.randint(0, len(specific_list)-1)]