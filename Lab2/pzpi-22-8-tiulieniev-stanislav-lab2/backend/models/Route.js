const mongoose = require('mongoose');
const RouteSchema = new mongoose.Schema({
  pos_start: String,
  pos_finish: String,
  vehicle_id: { type: mongoose.Schema.Types.ObjectId, ref: 'Vehicle' },
});
module.exports = mongoose.model('Route', RouteSchema);
