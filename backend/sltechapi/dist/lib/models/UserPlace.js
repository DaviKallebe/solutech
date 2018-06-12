"use strict";
const sequelize_1 = require("sequelize");
const mysql_1 = require("../mysql");
exports.UserPlace = mysql_1.sequelize.define('usuario_lugar', {
    id_lugar: {
        type: sequelize_1.default.BIGINT(10).UNSIGNED,
        autoIncrement: true,
        allowNull: false,
        primaryKey: true
    },
    descricao: {
        type: sequelize_1.default.TEXT('tiny'),
        allowsNull: false
    },
    cep: {
        type: sequelize_1.default.STRING(10)
    },
    logradouro: {
        type: sequelize_1.default.STRING(40)
    },
    estado: {
        type: sequelize_1.default.STRING(20)
    },
    cidade: {
        type: sequelize_1.default.STRING(20)
    },
    bairro: {
        type: sequelize_1.default.STRING(30)
    },
    numero: {
        type: sequelize_1.default.INTEGER.UNSIGNED
    },
    complemento: {
        type: sequelize_1.default.STRING(15)
    },
    latitude: {
        type: sequelize_1.default.DECIMAL(10, 8)
    },
    longitude: {
        type: sequelize_1.default.DECIMAL(11, 8)
    }
});
//# sourceMappingURL=UserPlace.js.map