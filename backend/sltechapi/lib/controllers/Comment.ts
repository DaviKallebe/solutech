import { Request, Response } from 'express';
import { sequelize } from "../mysql";

export class CommentController {
    public getComments(req: Request, res: Response) {
        sequelize.query("SELECT perfils.primeiroNome, perfils.ultimoNome, perfils.imagem, \
            \ncomentarios.comentario, comentarios.ponto FROM comentarios, perfis AS perfils \
            \nWHERE comentarios.destinatario = ? AND comentarios.remetente = perfils.id_user",
            { replacements: [req.params.id_user], type: sequelize.QueryTypes.SELECT }
        ).then(result => {
            res.status(200).json(result);
        }, err =>{
            res.status(500).json(err);
        });
    }
}
