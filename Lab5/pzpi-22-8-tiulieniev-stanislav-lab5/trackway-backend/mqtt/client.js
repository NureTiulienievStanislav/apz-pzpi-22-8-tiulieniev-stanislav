const mqtt = require('mqtt');
const SensorData = require('../models/SensorData');
const client = mqtt.connect('mqtt://broker.hivemq.com');

client.on('connect', () => {
  client.subscribe('trackway/sensors/#');
});

client.on('message', async (topic, message) => {
  const data = JSON.parse(message.toString());
  const { vehicleId, frontDistance, rearDistance } = data;

  await SensorData.create({ vehicleId, frontDistance, rearDistance });
});

module.exports = client;
