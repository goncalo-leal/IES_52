#!/usr/bin/env python
import pika
import json

class Sender:
    def __init__(self) -> None:
        self.host = "https://192.168.160.238/"
        self.port = "5672"
        self.queue = "QUEUE"
        self.exchange = "EN"
        self.routing_key = ""

        self.connection = pika.BlockingConnection(
            pika.ConnectionParameters(host=self.host, port=self.port)
        )
        self.channel = self.connection.channel()
        self.channel.queue_declare(queue=self.queue, durable=True)

    def send(self, msg):
        to_send = json.dumps(msg)

        self.channel.basic_publish(
            exchange = self.exchange, 
            routing_key = self.routing_key, 
            body = to_send)
        print(" [x] Sent '", to_send,"'")

    def close_connection(self):
        self.connection.close()