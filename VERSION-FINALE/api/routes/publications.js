const express = require('express');
const router = express.Router();
const Publication = require('../models/publication').publicationModel;
const Sector = require('../models/sector');
var multer = require('multer');
var path = require('path');
var fs = require('fs');
const mongoose = require('mongoose');
var async = require('async');

// upload file (image || video) via multer
var storageFile = multer.diskStorage({
    destination: function (req, file, cb) {
        if (path.extname(file.originalname) == ".mp4"){
            cb(null, 'uploads/videos/')
        }else{
            cb(null, 'uploads/images/publications/')
        }
        
    },
    filename: function (req, file, cb) {
        cb(null, Date.now() + path.extname(file.originalname)) //Appending extension
    }
});
const uploadFile = multer({storage: storageFile});

var pathFolderImagesPublications = 'uploads/images/publications/';
var pathFolderVideosPublications = 'uploads/videos/';

// add publication
router.post('/AddPublication',multer({
    storage: storageFile
})  .single('file'), function (req, res) {
    try {
        var newPublication = new Publication();
        if (req.file){
            newPublication.name_file = req.file.filename
            newPublication.type_file = req.body.type_file;
        }
        newPublication.title = req.body.title;
        newPublication.textPub = req.body.textPub;
        newPublication.rue = req.body.rue;
        newPublication.ville = req.body.ville;
        newPublication.gouvernaurat = req.body.gouvernaurat;
        newPublication.latitude = req.body.latitude;
        newPublication.longitude = req.body.longitude;



        newPublication.nom= req.body.nom;
        //newPublication.sector = req.body.sectorId;
        newPublication.owner = req.body.ownerId;
       
        newPublication.createdAt = Date.now();
        //save the publication
        newPublication.save(function (err, savedPublication) {
        if (err) {
            res.json({
                status: 2,
                message: err
            });
        } else {
            res.json({
                status: 1,
                message: 'Publication added successfully',
                data: savedPublication.getPublication()
            })
        }
    });
    } catch (err) {
        res.json({
            status: 0,
            message: '500 Internal Server Error',
            data: {}
        })

    }
});

// get Publication By Id
router.post('/getPublicationById', function (req, res) {
    try {

        Publication.findOne({'_id': req.body.publicationId}).populate('owner',{'firstName': 1,'lastName':1,'photo':1}).populate('sector').exec(function (err, publication) {
            if (err) {
                return res.json({
                    status: 0,
                    message: ('error get Publication ' + err)
                });
            }
            if (!publication) {
                return res.json({
                    status: 0,
                    message: ('Publication does not exist')
                });
            }
             else {
                var pubDetails = publication.getPublication()
                // test if user Connected like || dislike publication
                if (req.body.userIdConnected){
                    // parcourir liste des j'aime à partir du résultat détaillée (of findOne)
                    async.forEachOf(publication.likes, function (like, index, next) {
                        if (like.user == req.body.userIdConnected){
                            pubDetails.isLiked = true;
                        }
                        next();
                    })
                }
                
                res.json({
                    status: 1,
                    message: 'get Publication successfully',
                    data: {
                        publication: pubDetails
                    }
                });
            }
        });
    

    } catch (err) {
        console.log(err);
        res.json({
            status: 0,
            message: '500 Internal Server Error',
            data: {}
        })

    }
});

// get Publications By Owner with pagination
router.post('/getAllPublicationsByOwner', function (req, res) {
    try {
        var perPage = 10, page = 1;
        if (req.body.perPage !== undefined) {
            perPage = parseInt(req.body.perPage);
        }
        if (req.body.page !== undefined) {
            page = parseInt(req.body.page);
        }
        Publication
        .find({'owner': req.body.ownerId}, {}, {
            sort: {'createdAt': -1},
            skip: (perPage * page) - perPage,
            limit: perPage})
        .populate('owner',{'firstName': 1,'lastName':1,'photo':1})
        .populate('sector')
        .exec(function (err, publications) {
            if (err) {
                return res.json({
                    status: 0,
                    message: ('error get Publication ' + err)
                });
            }
            else {
                var allPublicationsDetails = []
                async.forEachOf(publications, function (publication, index, next) {
                   
                    var pubDetails = publication.getPublication()
                    // test if user Connected like || dislike publication
                    if (req.body.userIdConnected){
                        // parcourir liste des j'aime à partir du résultat détaillée (of findOne)
                        async.forEachOf(publication.likes, function (like, index, next) {
                            if (like.user == req.body.userIdConnected){
                                pubDetails.isLiked = true;
                            }
                            next();
                        })
                    }
                    
                    allPublicationsDetails.push(pubDetails);
                    next();
                })
                // configs is now a map of JSON data
                Publication.find({'owner': req.body.ownerId}).exec(function (err, count) {
                    res.json({
                        status: 1,
                        message: 'get Publications By owner successfully',
                     
                            publication: publication,
                        
                    });
                });
            }
        });
    } catch (err) {
        console.log(err);
        res.json({
            status: 0,
            message: '500 Internal Server Error',
            data: {}
        })

    }
});

// delete publication by Id
router.post('/deletePublication', function (req, res) {
    try {

        Publication.findOne({'_id': req.body.publicationId}).exec(function (err, publication) {
            if (err) {
                return res.json({
                    status: 0,
                    message: ('error get Publication ' + err)
                });
            }
            if (!publication) {
                return res.json({
                    status: 0,
                    message: ('Publication does not exist')
                });
            }
            else {
                // delete file of publication
                if (publication.name_file) {
                    var fullPath;
                    // get full path of file (video || image)
                    if (publication.type_file == "video") {
                        fullPath = pathFolderVideosPublications + publication.name_file;
                    }else{
                        fullPath = pathFolderImagesPublications + publication.name_file;
                    }
                    fs.stat(fullPath, function (err, stats) {
                        if (err) {
                            return console.error(err);
                        }
                        fs.unlink((fullPath), function (err) {
                            if (err) return console.log(err);
                        });
                    });
                }
                publication.remove(function (err, publication) {
                    if (err) {
                        return res.json({
                            status: 0,
                            message: ('error delete Publication ' + err)
                        });
                    }
                    else {
                        res.json({
                            status: 1,
                            message: 'Publication deleted successfully',
                        });
                    }
                });

            }
        });
    

    } catch (err) {
        console.log(err);
        res.json({
            status: 0,
            message: '500 Internal Server Error',
            data: {}
        })

    }
});

//++++++++++++ CRUD COMMENTS ++++++++++++++++++

// add Comment to publication
router.post('/addComment', function (req, res) {
    try {
        Publication.findOne({'_id': req.body.publicationId}).exec(function (err, publication) {
            if (err) {
                return res.json({
                    status: 0,
                    message: ('Error find publication ') + err
                });
            } else {
                try {
                    var commentContent = [];
                    if (req.body.textPub) {
                        commentContent = publication.comments;
                        const comment = {
                            textPub: req.body.textPub,
                            author: req.body.userId,
                            date: Date.now(),
                        };
                        commentContent.push(comment);
                        publication.comments = commentContent;
                        publication.save(function (err) {
                            if (err) {
                                console.log('error' + err)
                            } else {
                                
                                return res.json({
                                    status: 1,
                                    message: 'Comment added succeffully ',
                                    commentId : publication.comments[publication.comments.length-1]._id,
                                    date: publication.comments[publication.comments.length-1].date
                                    
                                });
                            }
                        });
                    }
                } catch (err) {
                    console.log(err);
                    res.json({
                        status: 0,
                        message: '500 Internal Server Error',
                        data: {}
                    })

                }
            }
        });

    } catch (err) {
        console.log(err);
        res.json({
            status: 0,
            message: '500 Internal Server Error',
            data: {}
        })

    }
});

// add location


// delete comment 
router.post('/deleteComment', function (req, res) {
    Publication.findOne({'_id':req.body.publicationId},function (err,publication) {
        if (err) {
            return res.json({
                status: 0,
                message: ('Error find publication ') + err
            });
        } else {
            for (var i = 0; i < publication.comments.length; i++) {
                if(publication.comments[i]._id==req.body.commentId)
                {
                    publication.comments.splice(i,1);
                }
            }
            publication.save();
            res.json({
                status: 1,
                message : 'comment succefuuly deleted'
            });
        }
    });
});

// get all comments of publication with pagination
router.post('/getCommentsByPublication', function (req, res) {
    try {
        var perPage = 10, page = 1;
        if (req.body.perPage !== undefined) {
            perPage = parseInt(req.body.perPage);
        }
        if (req.body.page !== undefined) {
            page = parseInt(req.body.page);
        }
        var option = {"_id": mongoose.Types.ObjectId(req.body.publicationId)}
        Publication.aggregate([
            {"$unwind": "$comments"},
            {"$match": option},
            {"$sort": {"_id": 1, "comments.date": -1}},
            {"$skip": (perPage * page) - perPage}, {"$limit": perPage},
            {"$group": {"_id": "$_id", "comments": {"$push": "$comments"}}}

        ]).exec(function (err, comment) {
            if (err) {
                res.json({
                    status: 0,
                    message: ('erreur get all Comment ') + err
                });
            } else {
                try {
                    Publication.findOne({_id: req.body.publicationId}).exec(function (err, count) {
                        if (err) {
                            res.json({
                                status: 0,
                                message: ('erreur get count publication ') + err
                            });
                        } else {
                            try {
                                Publication.populate(comment, {
                                    path: 'comments.author',
                                    select: '_id firstName lastName photo pictureProfile'
                                }, function (err, populatedComment) {
                                    // Your populated translactions are inside populatedTransactions

                                    var commentsList = [];
                                    if (populatedComment.length > 0) {
                                        commentsList = populatedComment[0].comments;
                                    }
                                    if (count) {
                                        if (count.comments) {
                                            var countComments;
                                            countComments = count.comments;
                                            res.json({
                                                status: 1,
                                                message: 'get All Comments succeffully',
                                           
                                                    comments: commentsList,
                                                    currentPage: page,
                                                    totalPages: Math.ceil(countComments.length / perPage)
                                                
                                            });
                                        }
                                    } else {
                                        res.json({
                                            status: 1,
                                            message: 'get All Comments succeffully',
                                            data: {
                                                comments: commentsList,
                                                current: page,
                                                pages: 1
                                            }
                                        });
                                    }
                                });
                            } catch (err) {
                                console.log(err);
                                res.json({
                                    status: 0,
                                    message: '500 Internal Server Error',
                                    data: {}
                                })

                            }
                        }
                    });
                } catch (err) {
                    console.log(err);
                    res.json({
                        status: 0,
                        message: '500 Internal Server Error',
                        data: {}
                    })

                }
            }
        });
    } catch (err) {
        console.log(err);
        res.json({
            status: 0,
            message: '500 Internal Server Error',
            data: {}
        })

    }
});


//++++++++++++ CRUD LIKES ++++++++++++++++++

// add like to publication
router.post('/addLikeToPublication', function (req, res) {
    try {
        console.log(req.body);
        Publication.findOne({'_id': req.body.publicationId}).exec(function (err, publication) {
            if (err) {
                return res.json({
                    status: 0,
                    message: ('Error find publication ') + err
                });
            } else {
                try {
                    var likeContent = [];
                        likeContent = publication.likes;
                        const like = {
                            user: req.body.userId,
                            date: Date.now()
                        };
                        likeContent.push(like);
                        publication.likes = likeContent;
                        publication.save(function (err) {
                            if (err) {
                                console.log('error' + err)
                            } else {
                                return res.json({
                                    status: 1,
                                    message: 'Publication is liked succeffully'
                                });
                            }
                        });
                    
                } catch (err) {
                    console.log(err);
                    res.json({
                        status: 0,
                        message: '500 Internal Server Error',
                        data: {}
                    })

                }
            }
        });

    } catch (err) {
        console.log(err);
        res.json({
            status: 0,
            message: '500 Internal Server Error',
            data: {}
        })

    }
});

// dislike Publication (delete object like from list)  
router.post('/dislikePublication', function (req, res) {
    Publication.findOne({'_id':req.body.publicationId},function (err,publication) {
        if (err) {
            return res.json({
                status: 0,
                message: ('Error find publication ') + err
            });
        } else {
            for (var i = 0; i < publication.likes.length; i++) {
                if(publication.likes[i].user == req.body.userId)
                {
                    publication.likes.splice(i,1);
                }
            }
            publication.save();
            res.json({
                status: 1,
                message : 'Publication is disliked succeffully'
            });
        }
    });
});

// get list likes of publication with pagination
router.post('/getListLikesByPublication', function (req, res) {
    try {
        var perPage = 10, page = 1;
        if (req.body.perPage !== undefined) {
            perPage = parseInt(req.body.perPage);
        }
        if (req.body.page !== undefined) {
            page = parseInt(req.body.page);
        }
        var option = {"_id": mongoose.Types.ObjectId(req.body.publicationId)}
        Publication.aggregate([
            {"$unwind": "$likes"},
            {"$match": option},
            {"$sort": {"_id": 1, "likes.date": -1}},
            {"$skip": (perPage * page) - perPage}, {"$limit": perPage},
            {"$group": {"_id": "$_id", "likes": {"$push": "$likes"}}}

        ]).exec(function (err, like) {
            if (err) {
                res.json({
                    status: 0,
                    message: ('erreur get liste likes') + err
                });
            } else {
                try {
                    Publication.findOne({_id: req.body.publicationId}).exec(function (err, count) {
                        if (err) {
                            res.json({
                                status: 0,
                                message: ('erreur get count publication ') + err
                            });
                        } else {
                            try {
                                Publication.populate(like, {
                                    path: 'likes.user',
                                    select: '_id firstName lastName photo pictureProfile'
                                }, function (err, populatedLike) {
                                    // Your populated translactions are inside populatedTransactions

                                    var likesList = [];
                                    if (populatedLike.length > 0) {
                                        likesList = populatedLike[0].likes;
                                    }
                                    if (count) {
                                        if (count.likes) {
                                            var countLikes;
                                            countLikes = count.likes;
                                            res.json({
                                                status: 1,
                                                message: 'get liste likes succeffully',
                                                
                                                    likes: likesList,
                                                    currentPage: page,
                                                    totalPages: Math.ceil(countLikes.length / perPage)
                                                
                                            });
                                        }
                                    } else {
                                        res.json({
                                            status: 1,
                                            message: 'get liste likes succeffully sssss',
                                            data: {
                                                likes: likesList,
                                                currentPage: page,
                                                totalPages: 1
                                            }
                                        });
                                    }
                                });
                            } catch (err) {
                                console.log(err);
                                res.json({
                                    status: 0,
                                    message: '500 Internal Server Error',
                                    data: {}
                                })

                            }
                        }
                    });
                } catch (err) {
                    console.log(err);
                    res.json({
                        status: 0,
                        message: '500 Internal Server Error',
                        data: {}
                    })

                }
            }
        });
    } catch (err) {
        console.log(err);
        res.json({
            status: 0,
            message: '500 Internal Server Error',
            data: {}
        })

    }
});

//++++++++++++ MODULE SEARCH ++++++++++++++++++
// advanced search (with out pagination)
router.get('/searchKey', function (req, res) {
    try {
        async.series([
            // return the sectors of which titles of publications begin with sent word 
                function (callback) {
                    try {
                        Publication.find({
                            title: {
                                $regex: '\\b' + req.query.q,
                                $options: "gi"
                            }
                        }).distinct('sector').exec(function (err, ids) {
                            if (err) {
                                return res.json({
                                    status: 0,
                                    message: ('Error while getting publications') + err
                                });

                            } else {
                                try {
                                    Sector.find({'_id': {$in: ids}}, function (err, result) {

                                        callback(null, result);
                                    });
                                } catch (err) {
                                    console.log(err);
                                    res.json({
                                        status: 0,
                                        message: '500 Internal Server Error',
                                        data: {}
                                    })

                                }

                            }
                        });
                    } catch (err) {
                        console.log(err);
                        res.json({
                            status: 0,
                            message: '500 Internal Server Error',
                            data: {}
                        })

                    }
                },
                // return all publication which title begin with sent word (max 10 pub)
                function (callback) {
                    try {
                        Publication.find({title: {$regex: '\\b' + req.query.q, $options: "gi"}}, {
                            title: 1,
                            sector: 1
                        }).populate('sector').limit(10).exec(function (err, publications) {
                            if (err) {
                                return res.json({
                                    status: 0,
                                    message: ('Error while getting publications') + err
                                });

                            } else {
                                callback(null, publications);

                            }
                        });
                    } catch (err) {
                        console.log(err);
                        res.json({
                            status: 0,
                            message: '500 Internal Server Error',
                            data: {}
                        })

                    }
                }
            ],
            // optional callback
            function (err, results) {
                if (err) {
                    return res.json({
                        status: 0,
                        message: ('Error while getting publications') + err
                    });

                } else {
                    res.json({
                        status: 1,
                        data: {
                            sectors: results[0],
                            publication: results[1]
                        }

                    });
                }
            });
    } catch (err) {
        console.log(err);
        res.json({
            status: 0,
            message: '500 Internal Server Error',
            data: {}
        })

    }
});

// search pub by title in specific sector (with pagination)
router.post('/searchPubInSector', function (req, res) {
    var perPage = 10, page = 1;
    if (req.body.perPage !== undefined) {
        perPage = parseInt(req.body.perPage);
    }
    if (req.body.page !== undefined) {
        page = parseInt(req.body.page);
    }
    if (req.body.sectorId) {
        if ((req.body.sectorId).length > 0) {
            var option = {
                title: {$regex: '\\b' + req.body.q, $options: "gi"},
                sector: req.body.sectorId
            };
        }
    } else {
        var option = {
            title: {$regex: '\\b' + req.body.q, $options: "gi"}
        };
    }
    Publication.find(option, {}, {
            sort: {'createdAt': -1},
            skip: (perPage * page) - perPage,
            limit: perPage})
        .populate('owner',{'firstName': 1,'lastName':1,'photo':1})
        .populate('nom')
        .exec(function (err, publications) {
        if (err) {
            return res.json({
                status: 0,
                message: ('Error while getting publications') + err
            });

        } else {
            var allPublicationsDetails = []
            async.forEachOf(publications, function (publication, index, next) {
               
                var pubDetails = publication.getPublication()
                // test if user Connected like || dislike publication
                if (req.body.userIdConnected){
                    // parcourir liste des j'aime à partir du résultat détaillée (of findOne)
                    async.forEachOf(publication.likes, function (like, index, next) {
                        if (like.user == req.body.userIdConnected){
                            pubDetails.isLiked = true;
                        }
                        next();
                    })
                }
                
                allPublicationsDetails.push(pubDetails);
                next();
            })
            Publication.find(option).exec(function (err, count) {
                if (err) {
                    console.error(err.message)
                }
                res.json({
                    status: 1,
                    data: {
                        publications: allPublicationsDetails,
                        currentPage: page,
                        Totalpages: Math.ceil(count.length / perPage)
                    }

                });
            });
        }
    })
})

//++++++++++++ TIMELINE REQUEST++++++++++++++++++

// get All Publications order by date  with pagination
router.post('/getAllPublicationsForTimeLine', function (req, res) {
    try {
        var perPage = 10, page = 1;
        if (req.body.perPage !== undefined) {
            perPage = parseInt(req.body.perPage);
        }
        if (req.body.page !== undefined) {
            page = parseInt(req.body.page);
        }
        Publication
        .find({},{},{
            sort: {'createdAt': -1},
            skip: (perPage * page) - perPage,
            limit: perPage})
        .populate('owner',{'firstName': 1,'lastName':1,'photo':1})
        .populate('sector')
        .exec(function (err, publications) {
            if (err) {
                return res.json({
                    status: 0,
                    message: ('error get Publication ' + err)
                });
            }
            else {
                var allPublicationsDetails = []
                async.forEachOf(publications, function (publication, index, next) {
                   
                    var pubDetails = publication.getPublication()
                    // test if user Connected like || dislike publication
                    if (req.body.userIdConnected){
                        // parcourir liste des j'aime à partir du résultat détaillée (of findOne)
                        async.forEachOf(publication.likes, function (like, index, next) {
                            if (like.user == req.body.userIdConnected){
                                pubDetails.isLiked = true;
                            }
                            next();
                        })
                    }
                    
                    allPublicationsDetails.push(pubDetails);
                    next();
                })
                // configs is now a map of JSON data
                Publication.find().exec(function (err, count) {
                    res.json({
                        status: 1,
                        message: 'get All Publications successfully',
                        data: {
                            publications: allPublicationsDetails,
                            currentPage: page,
                            Totalpages: Math.ceil(count.length / perPage)
                        }
                    });
                });
            }
        });
    } catch (err) {
        console.log(err);
        res.json({
            status: 0,
            message: '500 Internal Server Error',
            data: {}
        })

    }
});


router.get('/getAllPub', function (req, res) {
    try {
        Publication.find().populate('owner',{'firstName': 1,'lastName':1,'photo':1}).exec(function (err, publication) {
            if (err) {
                res.json({
                    status: 0,
                    message: ('error get list sectors.' + err)

                });

            } else {
                // return nbr publications for each sector

                res.json({
                    status: 1,
                    message: 'get list sectors successfully',
                
                        publication: publication,
                    

                });

            }

        });
    } catch (err) {
        console.log(err);
        res.json({
            status: 0,
            message: '500 Internal Server Error',
            data: {}
        })

    }
});


router.post('/getPubByUser', function (req, res) {
    try {
        Publication.find({'owner': req.body.ownerId}).populate('owner',{'firstName': 1,'lastName':1,'photo':1}).exec(function (err, publication) {
            if (err) {
                res.json({
                    status: 0,
                    message: ('error get list sectors.' + err)

                });

            } else {
                // return nbr publications for each sector

                res.json({
                    status: 1,
                    message: 'get list sectors successfully',
                
                        publication: publication,
                    

                });

            }

        });
    } catch (err) {
        console.log(err);
        res.json({
            status: 0,
            message: '500 Internal Server Error',
            data: {}
        })

    }
});

module.exports = router;