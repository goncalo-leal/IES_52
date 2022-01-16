import json
import requests
# response = requests.get("https://randomuser.me/api/")
# response.text

class ApiConnector:
    def __init__(self) -> None:
        self.api_url = "http://localhost:8080/mq/" # "http://192.168.160.238:6868/mq/"

    def get_shoppings_list(self):
        method = "Shoppings"
        response = requests.get(self.api_url+method)
        return json.loads(response.text)

    def get_shopping_sensors(self, shopping_id):
        method = "ShoppingAllSensors"
        response = requests.get(self.api_url+method+"/"+str(shopping_id))
        return json.loads(response.text)

    def get_sensor_limit(self, sensor_id):
        method = "getMaxPossible"
        response = requests.get(self.api_url+method+"/"+str(sensor_id))
        return json.loads(response.text)
