"use strict";
const sequelize_1 = require("sequelize");
const crypto_1 = require("crypto");
const mysql_1 = require("../mysql");
exports.User = mysql_1.sequelize.define('user', {
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
    password: {
        type: sequelize_1.default.STRING(512)
    },
    salt: {
        type: sequelize_1.default.STRING(512)
    },
    facebook_id: {
        type: sequelize_1.default.BIGINT.UNSIGNED,
        unique: true
    },
    facebook_email: {
        type: sequelize_1.default.STRING(30),
        unique: true
    },
    account_creation: {
        type: sequelize_1.default.INTEGER.UNSIGNED
    }
}, {
    hooks: {
        afterCreate: (user, options) => {
            if (user.password && user.password != "") {
                crypto_1.randomBytes(256, (err, buf) => {
                    if (err)
                        throw err;
                    crypto_1.pbkdf2(user.password, buf.toString('hex'), 1, 256, 'sha512', (err, derivedKey) => {
                        if (err)
                            throw err;
                        user.password = derivedKey.toString('hex');
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
    return this.password === crypto_1.pbkdf2Sync(password, this.salt, 1, 256, 'sha512').toString('hex');
};
exports.User.sync({ force: false }).then(() => {
});
//# sourceMappingURL=Usuario.js.map