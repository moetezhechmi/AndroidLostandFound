const express = require('express');
const router = express.Router();
const Chat = require('../models/chat').chatModel;
var multer = require('multer');
var path = require('path');
var fs = require('fs');
var jwt = require('jsonwebtoken');
const mongoose = require('mongoose');
var async = require('async');
const usermo = require('../models/user');




router.post('/addRoom' ,function (req, res) {
    try {
        Chat.findOne({'idRoom': req.body.idRoom}, function (err, chat) {
            if (err) {
                res.json({
                    status: 0,
                    message: ('Error while saving') + err
                });
            }
            if (chat) {
                res.json({
                    status: 0,
                    message: ('room already used'),
                    idRoom:req.body.idRoom
                    
                });
            } else {

                    var newChat = new Chat({

                        user1: req.body.user1,
                        user2: req.body.user2,
                        idRoom:req.body.idRoom,                        
                        createdAt: Date.now()
                    });
                    //res.redirect("/uploads/images/users/" + req.file.filename);
                         
                    
                    
                    //save the user
                    newChat.save(function (err, savedChat) {
                        if (err) {
                            res.json({
                                status: 0,
                                message: err
                            });
                        } else {
                            res.json({
                                status: 1,
                                message: 'Room added successfully',
                                data: {
                                    chat:newChat
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



router.post('/SearchRoomIfExist', function (req, res) {
    Chat.findOne({'user1': req.body.user1}).findOne({'user2':req.body.user2}).exec(function (err, chat){
        if (err) {
            res.json({
                status: 0,
                message: ('Error while saving') + err
            });
        }
        if (!chat) {
            return res.json({
                status: 0,
                message: ('room does not exist')
            });
        }

        else{

            var chatDetails = chat.getChat()

            res.json({
                status: 1,
                message: 'get room successfully',
                data: {
                    chat: chatDetails
                }
            });

        }
        

    });

});



router.post('/SearchChat', function (req, res) {
    Chat.findOne({'user1': req.body.user1}).populate('user2',{'firstName': 1,'lastName':1,'photo':1}).exec(function (err, chat){
        if (err) {
            res.json({
                status: 0,
                message: ('Error while saving') + err
            });
        }
        if (!chat) {
            return res.json({
                status: 0,
                message: ('room does not exist')
            });
        }

        else{

            var chatDetails = chat.getUser2()

            res.json({
                status: 1,
                message: 'get room successfully',
              
                    user2: chat.user2,
                    idRoom:chat.idRoom

                
            });

        }
        

    });

});


module.exports = router;