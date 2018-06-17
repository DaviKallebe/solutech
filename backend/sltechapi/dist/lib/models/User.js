"use strict";
const sequelize_1 = require("sequelize");
const crypto_1 = require("crypto");
const mysql_1 = require("../mysql");
const UserPlace_1 = require("../models/UserPlace");
const UserProfile_1 = require("../models/UserProfile");
const UserHost_1 = require("../models/UserHost");
const Pet_1 = require("../models/Pet");
exports.User = mysql_1.sequelize.define('usuario', {
    id_user: {
        type: sequelize_1.default.BIGINT(10).UNSIGNED,
        autoIncrement: true,
        allowNull: false,
        primaryKey: true
    },
    username: {
        type: sequelize_1.default.STRING(20),
        unique: true
    },
    email: {
        type: sequelize_1.default.STRING(30),
        unique: true
    },
    pword: {
        type: sequelize_1.default.STRING(512)
    },
    salt: {
        type: sequelize_1.default.STRING(512)
    },
    facebookId: {
        type: sequelize_1.default.BIGINT.UNSIGNED,
        unique: true
    },
    account_creation: {
        type: sequelize_1.default.INTEGER.UNSIGNED
    }
}, {
    hooks: {
        afterCreate: (user, options) => {
            if (user.pword && user.pword != "") {
                crypto_1.randomBytes(256, (err, buf) => {
                    if (err)
                        throw err;
                    crypto_1.pbkdf2(user.pword, buf.toString('hex'), 1, 256, 'sha512', (err, derivedKey) => {
                        if (err)
                            throw err;
                        user.pword = derivedKey.toString('hex');
                        user.account_creation = 1;
                        user.salt = buf.toString('hex');
                        user.save();
                    });
                });
            }
            else {
                user.account_creation = 2;
                user.save();
            }
        }
    }
});
exports.User.prototype.checkPassword = function (password) {
    var pass = new Buffer(password, 'binary');
    return this.pword === crypto_1.pbkdf2Sync(pass, this.salt, 1, 256, 'sha512').toString('hex');
};
exports.User.sync({ force: false }).then(() => {
    exports.User.hasOne(UserPlace_1.UserPlace, { foreignKeyConstraint: true, foreignKey: 'id_user' });
    UserPlace_1.UserPlace.belongsTo(exports.User, { foreignKeyConstraint: true, foreignKey: 'id_user' });
    UserPlace_1.UserPlace.sync({ force: false }).then(() => {
    });
    exports.User.hasOne(UserProfile_1.UserProfile, { foreignKeyConstraint: true, foreignKey: 'id_user' });
    UserProfile_1.UserProfile.belongsTo(exports.User, { foreignKeyConstraint: true, foreignKey: 'id_user' });
    UserProfile_1.UserProfile.sync({ force: false }).then(() => {
    });
    exports.User.hasOne(UserHost_1.UserHost, { foreignKeyConstraint: true, foreignKey: 'id_user' });
    UserHost_1.UserHost.belongsTo(exports.User, { foreignKeyConstraint: true, foreignKey: 'id_user' });
    UserHost_1.UserHost.sync({ force: false }).then(() => {
    });
    exports.User.hasMany(Pet_1.Pet, { foreignKeyConstraint: true, foreignKey: 'id_user' });
    Pet_1.Pet.belongsTo(exports.User, { foreignKeyConstraint: true, foreignKey: 'id_user' });
    Pet_1.Pet.sync({ force: false }).then(() => {
    });
});
//# sourceMappingURL=User.js.map