"use strict";
const sequelize_1 = require("sequelize");
const mysql_1 = require("../mysql");
exports.UserPlace = mysql_1.sequelize.define('logradouro', {
    id_lugar: {
        type: sequelize_1.default.BIGINT(10).UNSIGNED,
        autoIncrement: true,
        allowNull: false,
        primaryKey: true
    },
    cep: {
        type: sequelize_1.default.STRING(10)
    },
    rua: {
        type: sequelize_1.default.STRING(30)
    },
    estado: {
        type: sequelize_1.default.STRING(20)
    },
    cidade: {
        type: sequelize_1.default.STRING(30)
    },
    bairro: {
        type: sequelize_1.default.STRING(15)
    },
    numero: {
        type: sequelize_1.default.INTEGER.UNSIGNED
    },
    complemento: {
        type: sequelize_1.default.STRING(15)
    },
    logradouro: {
        type: sequelize_1.default.STRING(40)
    },
    latitude: {
        type: sequelize_1.default.DECIMAL(10, 8)
    },
    longitude: {
        type: sequelize_1.default.DECIMAL(11, 8)
    },
    descricao: {
        type: sequelize_1.default.TEXT("tiny"),
        allowsNull: false
    },
    imagem: {
        type: sequelize_1.default.TEXT("tiny"),
    }
});
//# sourceMappingURL=UserPlace.js.map