from PMS5003Sensor import PMS5003Sensor
from DHT11Sensor import DHT11Sensor
from flask import Flask, request, jsonify

app = Flask(__name__)
pms = PMS5003Sensor()
dht = DHT11Sensor(pin=24)

@app.route("/get-data")
def get_data():

    temperature, humidity = dht.read()
    pmdata = pms.read("/dev/ttyS0")
    data = {
        "temperature": temperature,
        "humidity": humidity,
        "pm10": pmdata[3],
        "pm25": pmdata[4],
        "pm100": pmdata[5],
    }
    return jsonify(data), 200

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=4014)