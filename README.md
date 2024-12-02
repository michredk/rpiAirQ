# Air Quality Monitoring System 
##### GoLang REST API, Raspberry Pi Zero W and Android App

I wanted to create a system that monitors air quality and environmental conditions using a Raspberry Pi Zero W and sends the data to an Android app. The system will collect data such as temperature, humidity, and air quality, and the app will display this information in real-time.  

Things needed for this project:  
* Computer or microcontroller (Raspberry Pi Zero W)
* Air quality, temperature and humidity sensors
* Method to send data to the app
* Android app for data visualization  

The Raspberry Pi Zero W features both Wi-Fi and Bluetooth, suports SSH out of the box and has enough power to run services like a REST API. Essentially, it provides everything I need to develop my project.

I started setting up my Raspberry Pi by creating a bootable image for the operating system. Choosing DietPi ensures that no unnecessary services are running, making it ideal for the constrained hardware of the Raspberry Pi Zero. This will allow my REST API to run as efficiently as possible.

Nothing would work without proper sensors to collect the parameters we are interested in. After reseaching available hardware and considering costs, I chose the DHT11, a temperature and humidity sensor, and the PMS5003, a dust sensor that allows you to monitor air cleanliness. The PMS5003 measures PM1.0, PM2.5 and PM10 particles.

The API will be responsible for sending sensor data to the app. Another decision I had to make was which technology to use for creating my REST API. I decided to use Go because it is lightweight and fast.  

