const express = require('express');
const router = express.Router();
const User = require('../models/user');

var jwt = require('jsonwebtoken');
var multer = require('multer');
var fs = require('fs');
var path = require('path');
crypto = require('crypto');


storage = multer.diskStorage({
    destination: 'uploads/images/users',
    filename: function(req, file, cb) {
      return crypto.pseudoRandomBytes(16, function(err, raw) {
        if (err) {
          return cb(err);
        }
        return cb(null, "" + (raw.toString('hex')) + (path.extname(file.originalname)));
      });
    }
  });

const upload = multer({storage: storage});
var pathFolder = 'uploads/images/users/';

// signup
router.post('/signUp' ,multer({
    storage: storage
  }).single('upload'),function (req, res) {
    try {
        User.findOne({'email': req.body.email}, function (err, user) {
            if (err) {
                res.json({
                    status: 0,
                    message: ('Error while saving') + err
                });
            }
            if (user) {
                res.json({
                    status: 0,
                    message: ('Email already used')
                });
            } else {

                    var newUser = new User({

                        firstName: req.body.firstName,
                        lastName: req.body.lastName,
                        email: req.body.email,
                        phone: req.body.phone,
                        password: req.body.password,
                        createdAt: Date.now(),
                    });
                    //res.redirect("/uploads/images/users/" + req.file.filename);
                         
                        newUser.photo = "/uploads/images/users/"+req.file.filename
                    
                    
                    //save the user
                    newUser.save(function (err, savedUser) {
                        if (err) {
                            res.json({
                                status: 0,
                                message: err
                            });
                        } else {
                            var token = jwt.sign(savedUser.getUser(), 'MySecret', {expiresIn: 3600});
                            res.json({
                                status: 1,
                                message: 'signUp successfully',
                                data: {
                                    user: savedUser.getUser(),
                                    token: token
                                }
                            })

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

//signin  user
router.post('/signIn', function (req, res) {
    try {
        User.findOne({email: req.body.email}, function (err, user) {
            if (err) {
                res.json({
                    status: 3,
                    message:"erreur"

                });
            }
            if (!user) {
                res.json({
                    status: 2,
                    message:"notFound"
                  
                });
            } else {
                // check if password matches
                user.comparePassword(req.body.password, function (err, isMatch) {
                    if (isMatch && !err) {
                        // if user is found and password is right create a token
                        var token = jwt.sign(user.getUser(), 'MySecret', {expiresIn: 3600});
                        res.json({
                            status: 8,
                            message:"success",

                    
                                user: user.getUser(),
                                token: token
                            
                        });
                    } else {
                        res.json({
                            status: 0,                   
                             message:"pass incorrect"

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

// update user
router.post('/updateUser',multer({
    storage: storage
}).single('upload'),function (req, res) {
    try {
        if (req.file) {
            User.findOne({_id: req.body.userId}, function (err, user) {
                if (user.photo !== undefined) {
                    var fullPath = pathFolder + user.photo;
                    fs.stat(fullPath, function (err, stats) {
                        if (err) {
                            return console.error(err);
                        }
                        // delete old image profile from floder
                        if (user.photo != "avatar.png"){
                            fs.unlink((fullPath), function (err) {
                                if (err) return console.log(err);
                                console.log('file deleted successfully');
                            });
                        }

                    });
                }
            });
        }
        User.findOne({_id: req.body.userId}, function (err, user) {
            if (err) {
                res.json({
                    status: 0,
                    message: ('Error update user') + err
                });
            } else {
                if (!user) {
                    res.json({
                        status: 0,
                        message: ('user does not exist')

                    });
                } else {
                    try {
                        if (req.body.email) {
                            user.email = req.body.email;
                        }
                        if (req.body.firstName) {
                            user.firstName = req.body.firstName;
                        }
                        if (req.body.lastName) {
                            user.lastName = req.body.lastName;
                        }
                        if (req.body.phone) {
                            user.phone = req.body.phone;
                        }
                        if (req.file) {
                            user.photo = req.file.filename;
                        }
                        if (req.body.oldPassword && req.body.newPassword) {
                            // check if password matches
                            user.comparePassword(req.body.oldPassword, function (err, isMatch, next) {
                                if (isMatch && !err) {
                                    user.password = req.body.newPassword;
                                    user.save(function (err, savedUser) {
                                        if (err) {
                                            res.json({
                                                status: 0,
                                                message: ('error Update user ') + err
                                            });
                                        } else {
                                            var token = jwt.sign(savedUser.getUser(), 'MySecret', {expiresIn: 36000});
                                            res.json({
                                                status: 1,
                                                message: 'Update user successfully',
                                                user: savedUser.getUser(),
                                                    token: token
                                                
                                            })
                                        }
                                    });
                                    
                                } else {
                                    res.json({
                                        status: 0,
                                        message: 'update user failed. Wrong password.'
                                    });
                                }
                            });
                        } else {
                        user.save(function (err, savedUser) {
                            if (err) {
                                res.json({
                                    status: 0,
                                    message: ('error Update user ') + err
                                });
                            } else {
                                var token = jwt.sign(savedUser.getUser(), 'MySecret', {expiresIn: 3600});
                                res.json({
                                    status: 1,
                                    message: 'Update user successfully',
                                    
                                        user: savedUser.getUser(),
                                        token: token
                                    
                                })
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


// get User By Id
router.post('/getUserById', function (req, res) {
    try {
        User.findOne({_id: req.body._id}, function (err, user) {
            if (err) {
                return res.json({
                    status: 0,
                    message: ('error get Profile ' + err)
                });
            }
            if (!user) {
                return res.json({
                    status: 0,
                    message: ('user does not exist')
                });
            }
             else {
                    res.json({
                        status: 1,
                        message: 'get Profile successfully',
                        data: {
                            user: user.getUser(),
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
/*xports.forgotPassword = (req, res) => {
    const {email} = req.body;

    User.findOne({email}, (err, User) => {
        if (err || !user){
            return res.status(400).json({error : "user with this mail does not exist"});
        }
        const token = jwt.sign({_id: user._id} , process.env.JWT_ACC_ACTIVATE , {expiresIn : '20min'});
        const data = {
            from: 'chaimahichri80@gmail.com',
            to: email,
            subject: 'account activation link',
            html: `
            <h2>please click on give link </h2>
            `

        };
        return data;
    })
}*/


module.exports = router;