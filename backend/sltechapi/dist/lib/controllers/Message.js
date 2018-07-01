"use strict";
const mysql_1 = require("../mysql");
class MessageController {
    getMessages(req, res) {
        mysql_1.sequelize.query('SELECT perfils.primeiroNome, perfils.ultimoNome, perfils.imagem, mensagens.mensagem, mensagens.data, mensagens.remetente FROM mensagens, perfils WHERE mensagens.destinatario = ? AND mensagens.remetente = perfils.id_user ORDER BY data', { replacements: [req.params.id_user], type: mysql_1.sequelize.QueryTypes.SELECT }).then(result => {
            res.status(200).json(result);
        }, err => {
            res.status(500).json(err);
        });
    }
}
exports.MessageController = MessageController;
//# sourceMappingURL=Message.js.map