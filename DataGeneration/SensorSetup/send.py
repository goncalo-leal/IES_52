#!/usr/bin/env python
import pika
import json

connection = pika.BlockingConnection(
    pika.ConnectionParameters(host='localhost', port='5672', credentials=pika.PlainCredentials('guest', 'guest')))
channel = connection.channel()

channel.queue_declare(queue='QUEUE', durable=True)

x = {
  "name": "John",
  "age": 30,
  "city": "New York"
}
json.dumps(x)

channel.basic_publish(exchange='EN', routing_key='', body=json.dumps(x))
print(" [x] Sent '", json.dumps(x),"'")
connection.close()