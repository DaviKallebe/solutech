import { Request, Response } from 'express';
import { sequelize } from "../mysql";
import { config } from '../../config';

export class CommentController {
    public getComments(req: Request, res: Response) {
        sequelize.query("SELECT perfils.primeiroNome, perfils.ultimoNome, perfils.imagem, \
            \ncomentarios.comentario, comentarios.ponto FROM comentarios, perfis AS perfils \
            \nWHERE comentarios.destinatario = ? AND comentarios.remetente = perfils.id_user",
            { replacements: [req.params.id_user], type: sequelize.QueryTypes.SELECT }
        ).then(result => {
            let array_list = result.map(comment => {
                if (comment.imagem != null)
                    comment.imagem = "http://" + config.serverIP + ":" + config.serverPort + "/" + comment.imagem.replace(/\\/gi, "/");

                return comment;
            })

            res.status(200).json(array_list);
        }, err =>{
            res.status(500).json(err);
        });
    }
}
