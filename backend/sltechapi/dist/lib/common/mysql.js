"use strict";
const config_1 = require("../config");
var Sequelize = require('sequelize');
var sequelize = new Sequelize(config_1.config.mysql.database, config_1.config.mysql.username, config_1.config.mysql.password, {
    host: config_1.config.mysql.host,
    dialect: 'mysql'
});
module.exports = sequelize;
//# sourceMappingURL=mysql.js.map