import { Message } from "../models/Message"
import { UserProfile } from "../models/UserProfile"
import { Request, Response } from 'express';
import Sequelize from 'sequelize';
import { sequelize } from "../mysql";

export class MessageController {
    public getMessages(req: Request, res: Response) {
        sequelize.query('SELECT perfils.primeiroNome, perfils.ultimoNome, perfils.imagem, mensagens.mensagem, mensagens.data, mensagens.remetente FROM mensagens, perfils WHERE mensagens.destinatario = ? AND mensagens.remetente = perfils.id_user ORDER BY data',
            { replacements: [req.params.id_user], type: sequelize.QueryTypes.SELECT }
        ).then(result => {
            res.status(200).json(result);
        }, err =>{
            res.status(500).json(err);
        });
    }
}
