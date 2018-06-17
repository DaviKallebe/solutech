"use strict";
const sequelize_1 = require("sequelize");
const mysql_1 = require("../mysql");
exports.UserProfile = mysql_1.sequelize.define('perfil', {
    primeiroNome: {
        type: sequelize_1.default.STRING(20),
        allowNull: false
    },
    ultimoNome: {
        type: sequelize_1.default.STRING(20),
        allowNull: false
    },
    documento: {
        type: sequelize_1.default.STRING(20),
        allowNull: false
    },
    orgaoEmissor: {
        type: sequelize_1.default.STRING(20),
        allowNull: false
    },
    cpf: {
        type: sequelize_1.default.STRING(15),
        allowNull: false
    },
    nascimento: {
        type: sequelize_1.default.DATE
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