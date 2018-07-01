"use strict";
const sequelize_1 = require("sequelize");
const mysql_1 = require("../mysql");
exports.Message = mysql_1.sequelize.define('mensagens', {
    remetente: {
        type: sequelize_1.default.BIGINT(10).UNSIGNED,
        allowNull: false
    },
    destinatario: {
        type: sequelize_1.default.BIGINT(10).UNSIGNED,
        allowNull: false
    },
    mensagem: {
        type: sequelize_1.default.TEXT('tiny'),
        allowNull: false
    },
    data: {
        type: sequelize_1.default.DATE,
        allowNull: false
    }
});
//# sourceMappingURL=Message.js.map