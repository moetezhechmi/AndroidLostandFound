const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const sectorSchema = new Schema({
    nameSector: {
        type: String,
        required: true        
    }
});

sectorSchema.methods.getSector=function () {
    return({
        _id: this._id,
        nameSector: this.nameSector,
    })
};


const Sector = module.exports = mongoose.model('Sector', sectorSchema);