const mongoose = require('mongoose');

const SensorDataSchema = new mongoose.Schema({
  vehicleId: mongoose.Schema.Types.ObjectId,
  timestamp: { type: Date, default: Date.now },
  frontDistance: Number,
  rearDistance: Number,
});

module.exports = mongoose.model('SensorData', SensorDataSchema);
