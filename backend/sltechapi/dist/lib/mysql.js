"use strict";
const config_1 = require("../config");
const sequelize_1 = require("sequelize");
exports.sequelize = new sequelize_1.default("", config_1.config.development.username, config_1.config.development.password, {
    dialect: "mysql"
});
if (config_1.config.force == true) {
    exports.sequelize.query('DROP DATABASE IF EXISTS ' + config_1.config.development.database + ';').then(data => {
    });
}
exports.sequelize.query('CREATE DATABASE IF NOT EXISTS ' + config_1.config.development.database + ';').then(data => {
});
exports.sequelize = new sequelize_1.default(config_1.config.development.database, config_1.config.development.username, config_1.config.development.password, {
    host: config_1.config.development.host,
    dialect: 'mysql'
});
//# sourceMappingURL=mysql.js.map