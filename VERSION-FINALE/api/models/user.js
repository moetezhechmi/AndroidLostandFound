const mongoose = require('mongoose');
const Schema = mongoose.Schema;
var bcrypt = require('bcrypt-nodejs');
var pathFolder = 'uploads/images/users/';
const config = require('../config');

const userSchema = new Schema({

    email: {
        type: String,
        required: true        
    },
    password: {
        type: String
    },

    firstName: {
        type: String
    },
    lastName: {
        type: String
    },
    phone: {
        type: String
    },
    photo: {
        type: String
    },
    createdAt : {
        type: Date,
        default: Date.now()
    },
    },
    {
    toJSON:{virtuals:true}
});

userSchema.methods.getUser=function () {
    return({
        _id: this._id,
        email: this.email,
        firstName: this.firstName,
        lastName: this.lastName,
        photo: this.photo,
        phone: this.phone,
        pictureProfile:this.pictureProfile,
        createdAt: this.createdAt
    })
};

userSchema.methods.getShortInfoUser=function () {
    return({
        _id: this._id,
        firstName: this.firstName,
        lastName: this.lastName,
        pictureProfile:this.pictureProfile
    })
};

userSchema.virtual('pictureProfile').get(function () {
    //return config.host+pathFolder + this.photo;
    if (this.photo !== undefined) {
  
        return config.host+pathFolder + this.photo;
    }else{
        return config.host+pathFolder +'avatar.png';
    }
});

userSchema.pre('save', function (next) {
    var user = this;
    if (this.isModified('password') || this.isNew) {
        bcrypt.genSalt(10, function (err, salt) {
            if (err) {
                return next(err);
            }

            bcrypt.hash(user.password, salt, null, function (err, hash) {
                if (err) {
                    return next(err);
                }
                if (user.password){
                user.password = hash;
                }
                next();
            });
        });
    } else {
        return next();
    }
});

userSchema.methods.comparePassword = function (passw, cb) {
    bcrypt.compare(passw, this.password, function (err, isMatch) {
        if (err) {
            return cb(err);
        }
        cb(null, isMatch);
    });
};

const User = module.exports = mongoose.model('User', userSchema);