"use strict";
const sequelize_1 = require("sequelize");
const mysql_1 = require("../mysql");
exports.Pet = mysql_1.sequelize.define('pet', {
    id_pet: {
        type: sequelize_1.default.BIGINT(10).UNSIGNED,
        autoIncrement: true,
        allowNull: false,
        primaryKey: true
    },
    nome: {
        type: sequelize_1.default.STRING(20)
    },
    idade: {
        type: sequelize_1.default.INTEGER
    },
    especie: {
        type: sequelize_1.default.INTEGER
    },
    raca: {
        type: sequelize_1.default.STRING(20)
    },
    tamanho: {
        type: sequelize_1.default.DECIMAL
    },
    peso: {
        type: sequelize_1.default.DECIMAL
    },
    sexo: {
        type: sequelize_1.default.INTEGER
    },
    vacinado: {
        type: sequelize_1.default.BOOLEAN
    },
    castrado: {
        type: sequelize_1.default.BOOLEAN
    },
    imagem: {
        type: sequelize_1.default.TEXT("tiny")
    },
    outros: {
        type: sequelize_1.default.TEXT("tiny")
    },
});
//# sourceMappingURL=Pet.js.map