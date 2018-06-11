"use strict";
const sequelize_1 = require("sequelize");
const mysql_1 = require("../mysql");
exports.User = mysql_1.sequelize.define('user', {
    id_user: {
        type: sequelize_1.default.BIGINT(10).UNSIGNED,
        autoIncrement: true,
        allowNull: false,
        primaryKey: true
    },
    username: {
        type: sequelize_1.default.STRING(20)
    },
    hased: {
        type: sequelize_1.default.STRING(512)
    },
    salt: {
        type: sequelize_1.default.STRING(64)
    },
    facebook_id: {
        type: sequelize_1.default.BIGINT.UNSIGNED
    },
    account_creation: {
        type: sequelize_1.default.INTEGER.UNSIGNED
    }
});
//# sourceMappingURL=crmUser.js.map