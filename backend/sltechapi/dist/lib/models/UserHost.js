"use strict";
const sequelize_1 = require("sequelize");
const mysql_1 = require("../mysql");
exports.UserHost = mysql_1.sequelize.define('hospedador', {
    likes: {
        type: sequelize_1.default.INTEGER.UNSIGNED,
        defaultValue: 0
    },
    dislikes: {
        type: sequelize_1.default.INTEGER.UNSIGNED,
        defaultValue: 0
    },
    perferenciaAnimal: {
        type: sequelize_1.default.STRING
    },
    quantidadeAnimal: {
        type: sequelize_1.default.STRING
    },
    tipoSupervisao: {
        type: sequelize_1.default.INTEGER
    },
    filhotes: {
        type: sequelize_1.default.BOOLEAN
    },
    idosos: {
        type: sequelize_1.default.BOOLEAN
    },
    macho: {
        type: sequelize_1.default.BOOLEAN
    },
    femea: {
        type: sequelize_1.default.BOOLEAN
    }
});
//# sourceMappingURL=UserHost.js.map