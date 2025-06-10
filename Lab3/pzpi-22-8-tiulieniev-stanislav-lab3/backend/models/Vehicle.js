const mongoose = require('mongoose');
const VehicleSchema = new mongoose.Schema({
  make: String,
  model: String,
  year: Number,
  vin: String,
  front_iot: String,
  back_iot: String,
});
module.exports = mongoose.model('Vehicle', VehicleSchema);
