#!/usr/bin/sh

sudo apt-get update
sudo apt install python3-tk
python3 -m pip install pysimplegui
python3 -m pip install pika --upgrade