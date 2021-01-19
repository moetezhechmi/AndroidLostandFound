const express = require('express');
const router = express.Router();
const Sector = require('../models/sector');
const Publication = require('../models/publication').publicationModel;
var async = require('async');
// //get all sectors
router.get('/getAllSectors', function (req, res) {
    try {
        Sector.find(function (err, sectors) {
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
                    data: {
                        sectors: sectors,
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

//get all sectors
router.get('/getAllSectorsWithNbrOccurences', function (req, res) {
    try {
        async.series([
            // return the occurences of each sector in all publications
                function (callback) {
                    try {
                         Publication.aggregate([
                             {"$group": {"_id": "$sector", "count" : {$sum : 1}}}
                         ]).exec(function (err, resultOccurence) {
                            callback(null, resultOccurence);
                         })
                    } catch (err) {
                        console.log(err);
                        res.json({
                            status: 0,
                            message: '500 Internal Server Error',
                            data: {}
                        })

                    }
                },
                // return all sector in dataBase
                function (callback) {
                    Sector.find(function (err, sectors) {
                        if (err) {
                            res.json({
                                status: 0,
                                message: ('error get list sectors.' + err)
            
                            });
            
                        } else {
                            callback(null, sectors);
                        }
            
                    });
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
                    var arrayOccurance =  results[0];
                    var arrayAllSectors = results[1];
                    var resultArray = [];

                    arrayAllSectors.forEach(sector => {
                        var count = 0;
                        index = arrayOccurance.map(function(e) { 
                            return (e._id).toString(); }).indexOf((sector._id).toString());
                        if (index != -1) {
                            count = arrayOccurance[index].count;
                        }
                        var element = {
                            "_id":sector._id,
                            "nameSector":sector.nameSector,
                            "count":count,
                        }
                        resultArray.push(element)
                    });

                    res.json({
                        status: 1,
                        data: {
                            sectors: resultArray,
                           
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

// the rest of all this web services are unused in mobile app but just for managment of sectors

// add sector
router.post('/addSector', function (req, res) {
    try {
        var newSector = new Sector({
            nameSector: req.body.nameSector,
        });
        //save the sector
        newSector.save(function (err, savedSector) {
        if (err) {
            res.json({
                status: 0,
                message: err
            });
        } else {
            res.json({
                status: 1,
                message: 'Sector added successfully',
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

// update sector
router.post('/updateSector', function (req, res) {
    try {
        Sector.findOne({_id: req.body.sectorId}, function (err, sector) {
            if (err) {
                res.json({
                    status: 0,
                    message: ('Error update sector') + err
                });
            } else {
                if (!sector) {
                    res.json({
                        status: 0,
                        message: ('sector does not exist')

                    });
                } else {
                    try {
                        if (req.body.nameSector) {
                            sector.nameSector = req.body.nameSector;
                        }
                        sector.save(function (err, savedSector) {
                            if (err) {
                                res.json({
                                    status: 0,
                                    message: ('error Update sector ') + err
                                });
                            } else {
                                res.json({
                                    status: 1,
                                    message: 'Update sector successfully',
                                    data: {
                                        sector: savedSector.getSector(),
                                    }
                                })
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

// delete sector 
router.post('/deleteSector', function (req, res) {
    try {
        Sector.findOne({_id: req.body.id}, function (err, sector) {

            if (err) {
                res.json({

                    status: 0,
                    message: ('Error')

                });
            } else {
                if (!sector) {
                    res.json({

                        status: 0,
                        message: ('Sector does not exist')

                    });
                } else {

                    sector.remove(function (err, sector) {
                        if (err) {
                            res.json({
                                status: 0,
                                message: ('Error')
                            });

                        }
                        else {
                            res.json({
                                status: 1,
                                message: ('Sector deleted successfully')

                            });
                        }

                    });
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

module.exports = router;