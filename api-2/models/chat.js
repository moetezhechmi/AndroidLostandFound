const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const config = require('../config');


const chatSchema = new Schema({
    idRoom: {
        type: String,      
    },
    user1: {
        type: Schema.ObjectId,
        ref: 'User'
    },

    user2: {
        type: Schema.ObjectId,
        ref: 'User'
    },
    
    createdAt:Date,
    
    },
    {
        toJSON:{virtuals:true}
    });



chatSchema.methods.getChat=function () {

    return({
        _id: this._id,
        user2: this.user2,
        user1: this.user1,
        idRoom:this.idRoom

       
    })
};

chatSchema.methods.getChatDetails=function () {
    return({
        _id: this._id,
        user2: this.user2,
        user1: this.user1,
        idRoom:this.idRoom
       
    })
};

chatSchema.methods.getUser2=function () {
    return({
        
        user2: this.user2,
        idRoom:this.idRoom

       
      
    })
};

var chatModel = mongoose.model('Chat', chatSchema);
module.exports = {
    chatModel : chatModel,
    chatSchema : chatSchema
};