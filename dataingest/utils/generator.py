import random
import string
import logging

logger = logging.getLogger(__name__)

class NotSupportedCustomer(Exception):
    pass

def generate_random_record(customer: str):
    if customer == "expedia":
        return {"checkInDateYear": random.randint(2000, 2021),
         "checkInDateMonth": random.randint(1, 12),
         "checkInDateDay": random.randint(1, 28),
         "destinationRegionId": nullable(str(random.randint(50000, 700000))),
         "destinationRegionName": get_random_destinationregionname(),
         "maxBudgetEuro": nullable(random.randint(100, 10000)/10),
         "propertyTypes": choose_specific_propertytype()}
    elif customer == "hellofresh":
        return {"numberOfPeople": random.randint(1, 6),
         "numberOfMeals": random.randint(3, 5),
         "foodAllergiesDescription": nullable(get_random_string(random.randint(10, 200))),
         "boxPrice": nullable(random.randint(40, 100)/10),
         "menuPreferenceTypes": choose_specific_menupreference()}
    else:
        raise NotSupportedCustomer(f"No customer implementation found for {customer}")


def nullable(fct):
    if random.randint(0, 2) == 2:
        return fct
    else:
        logger.debug("nullable introduced")
        return None


def get_random_destinationregionname():
    CHOICES = ["GHENT", "BRUSSELS", "ANTWERP", "BRUGES", "HASSELT", "OSTEND"]
    APPEND_CHOICES = ["- downtown", "> 1 km from citycentre", " (and vicinity)", " (and vicinity with limited access to public transport) "]
    return choose_random_from_list(CHOICES) + choose_random_from_list(APPEND_CHOICES)


def choose_specific_propertytype():
    CHOICES = ["Hotel", "Villa", "Apartment", "Condo", "Motel", "Bed_Breakfast", "Private_Vacation_Home"]
    return choose_random_from_list(CHOICES)


def choose_specific_menupreference():
    CHOICES = ["Meat", "Fish", "Vegan", "QuickMeal", "Family", "Vegetarian"]
    return choose_random_from_list(CHOICES)


def get_random_string(length):
    letters = string.ascii_lowercase
    result_str = ''.join(random.choice(letters) for i in range(length))
    return result_str


def choose_random_from_list(specific_list):
    return specific_list[random.randint(0, len(specific_list)-1)]