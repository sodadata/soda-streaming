import requests

url = "http://localhost:5000/api/v0.1/actions"
action_to_post = {"action_request": "partially_invalid", "topic_description": "travel"}

x = requests.post(url, json=action_to_post)

print(x)
