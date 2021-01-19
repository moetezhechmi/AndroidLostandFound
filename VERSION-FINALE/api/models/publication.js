const { relativeTimeThreshold } = require('moment');
const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const config = require('../config');
const Comment = require('./comment').commentSchema;
const Like = require('./like').likeSchema;
var pathFolderImagesPublications = 'uploads/images/publications/';
var pathFolderVideosPublications = 'uploads/videos/';

const publicationSchema = new Schema({
    title: {
        type: String,      
    },
    textPub: {
        type: String,      
    },
    rue: {
        type: String,      
    },
    ville: {
        type: String,      
    },
    gouvernaurat: {
        type: String,      
    },
    latitude: {
        type: String,      
    },
    longitude: {
        type: String,      
    },
    type_file: {
        type: String,  // image || video
    },
    name_file: {
        type: String,      
    },
    sector: {
        type: Schema.ObjectId,
        ref: 'Sector'
    },
    nom: {
        type: String, 
    },
    owner: {
        type: Schema.ObjectId,
        ref: 'User'
    },
    
    createdAt:Date,
    comments: [Comment],
    likes: [Like],
    },
    {
        toJSON:{virtuals:true}
    });

publicationSchema.virtual('url_file').get(function () {
    if (this.type_file == "image") {
    return config.host+pathFolderImagesPublications + this.name_file;
    }else if (this.type_file == "video"){
        return config.host+pathFolderVideosPublications + this.name_file;
    }
});

publicationSchema.methods.getPublication=function () {

    return({
        _id: this._id,
        title: this.title,
        textPub: this.textPub,
        type_file: this.type_file,
        name_file: this.name_file,
        url_file: this.url_file,
        rue: this.rue,
        ville: this.ville,
        gouvernaurat: this.gouvernaurat,
        latitude: this.latitude,
        longitude: this.longitude,
       // sector: this.sector,
        nom: this.nom,
        owner: this.owner,
        nbrComments: this.comments.length,
        nbrLikes: this.likes.length,
        isLiked: true,
        createdAt: this.createdAt,
    })
};

publicationSchema.methods.getPublicationDetails=function () {
    return({
        _id: this._id,
        title: this.title,
        textPub: this.textPub,
        type_file: this.type_file,
        name_file: this.name_file,
        url_file: this.url_file,
        rue: this.rue,
        ville: this.ville,
        gouvernaurat: this.gouvernaurat,
        latitude: this.latitude,
        longitude: this.longitude,
        //sector: this.sector,
        nom: this.nom,
        owner: this.owner,
        createdAt: this.createdAt,
    })
};

var publicationModel = mongoose.model('Publication', publicationSchema);
module.exports = {
    publicationModel : publicationModel,
    publicationSchema : publicationSchema
};