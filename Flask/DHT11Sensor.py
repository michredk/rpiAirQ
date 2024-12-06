import RPi.GPIO as GPIO
import dht11

class DHT11Sensor:
    def __init__(self, pin=24):
        GPIO.setwarnings(False)
        GPIO.setmode(GPIO.BCM)
        GPIO.cleanup()
        self.instance = dht11.DHT11(pin=pin)

    def read(self):
        while True:
            result = self.instance.read()
            if result.is_valid():
                return result.temperature, result.humidity
            else:
                print(f"Error: {result.error_code}")

    def cleanup(self):
        GPIO.cleanup()