const mongoose = require('mongoose');
const Schema = mongoose.Schema;
 
const likeSchema = new Schema ({
    date : {
        type: Date,
        default: Date.now()
    },
    user : {
        type: Schema.ObjectId,
        ref: 'User'
    },
});

likeSchema.methods.getLikes=function () {
    return({
        _id: this._id,
        date: this.date,
        user: userId

    })
};

var likeModel = mongoose.model('Like', likeSchema);
module.exports = {
    likeModel : likeModel,
    likeSchema : likeSchema
};