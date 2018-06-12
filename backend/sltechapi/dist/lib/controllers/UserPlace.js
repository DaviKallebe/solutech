"use strict";
const UserPlace_1 = require("../models/UserPlace");
class UserPlaceController {
    setPlace(req, res) {
        UserPlace_1.UserPlace.create(req.body).then(place => {
            res.json(place);
        }, err => {
            res.send(err);
        });
    }
    getPlace(req, res) {
        UserPlace_1.UserPlace.findOne({
            where: {
                id_user: req.params.id_user
            }
        }).then(place => {
            res.json(place);
        }, err => {
            res.send(err);
        });
    }
    updatePlace(req, res) {
        UserPlace_1.UserPlace.update(req.body, {
            where: {
                id_user: req.params.id_user
            }
        }).then(place => res.json(place)).catch(err => res.send(err));
    }
    existPlace(req, res) {
        UserPlace_1.UserPlace.findOne({
            where: {
                id_user: req.params.id_user
            }
        }).then(place => {
            if (place) {
                place.found = true;
                res.json(place);
            }
            else {
                let result = { found: false };
                res.json(result);
            }
        });
    }
}
exports.UserPlaceController = UserPlaceController;
//# sourceMappingURL=UserPlace.js.map