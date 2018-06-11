"use strict";
const User_1 = require("../models/User");
class UserController {
    loginNormal(req, res) {
        User_1.User.findOne({
            where: {
                username: req.query.username
            }
        }).then(user => {
            if (user && user.checkPassword(req.query.password)) {
                res.json(user);
            }
            else
                return res.send("Incorrect login");
        }, err => {
            res.send(err);
        });
    }
    createUserNormal(req, res) {
        User_1.User.create(req.body).then(() => {
            res.send('User created');
        }, err => {
            res.send(err);
        });
    }
}
exports.UserController = UserController;
//# sourceMappingURL=User.js.map