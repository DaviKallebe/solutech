"use strict";
const sequelize_1 = require("sequelize");
const mysql_1 = require("../mysql");
exports.UserProfile = mysql_1.sequelize.define('usuario_perfil', {
    nome: {
        type: sequelize_1.default.STRING(20),
        allowNull: false
    },
    idade: {
        type: sequelize_1.default.INTEGER.UNSIGNED
    },
    telefone: {
        type: sequelize_1.default.STRING(15)
    },
    descricao: {
        type: sequelize_1.default.TEXT('tiny')
    },
    imagem: {
        type: sequelize_1.default.STRING(255)
    }
});
//# sourceMappingURL=UserProfile.js.map