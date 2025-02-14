# Air Quality Monitoring System 
##### Python Flask REST API, Raspberry Pi Zero W and Android App

I developed a system that monitors air quality and environmental conditions using a Raspberry Pi Zero W and sends real-time data to an Android app. The system collects temperature, humidity, and air quality data, which the app displays for easy monitoring.

Components Used:
* Raspberry Pi Zero W as the central processing unit
* DHT11 sensor for temperature and humidity measurements
* PMS5003 dust sensor for air quality monitoring
* REST API for data transmission
* Android app for real-time visualization
  
The Raspberry Pi Zero W, with its built-in Wi-Fi and Bluetooth, was ideal for this project. Its support for SSH and ability to run a REST API made it the perfect choice for handling data collection and transmission.

To optimize performance on the limited hardware, I installed DietPi as the operating system. This lightweight OS ensured that only essential services were running, allowing the REST API to operate efficiently.

For environmental monitoring, I selected the DHT11 sensor to measure temperature and humidity, along with the PMS5003 dust sensor, which tracks air quality by detecting PM1.0, PM2.5, and PM10 particles.

Initially, I planned to build the REST API in Go due to its speed and efficiency. However, after struggling to find reliable sensor libraries, I switched to Python, which provided extensive support for the necessary hardware.
