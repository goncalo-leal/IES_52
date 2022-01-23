import RPi.GPIO as GPIO
import time
import datetime

from sender import Sender

sensor = 16

GPIO.setmode(GPIO.BOARD)
GPIO.setup(sensor, GPIO.IN)

print("IR Sensor is ready...")
sender = Sender()
try:
	counter = 0
	while True:
		if GPIO.input(sensor):
			counter += 1
			print(counter, "Object detected")
			ct = datetime.datetime.now().strftime("%Y-%m-%d-%H-%M-%S")

			msg = {"sensor_id": 4, "data": ct}
			sender.send(msg)
			while GPIO.input(sensor):
				time.sleep(0.05)
except KeyboardInterrupt:
	GPIO.cleanup()
