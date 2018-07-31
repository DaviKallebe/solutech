import { Request, Response } from 'express';
import { Hospedagem } from "../models/Hospedagem";
import { Comment } from "../models/Comment";
import { sequelize } from "../mysql";
import { config } from '../../config';

export class HospedagemController {
    public novaHospedagem(req: Request, res: Response) {
        Hospedagem.create(req.body).then(hospedagem => {
            res.status(200).json(hospedagem);
        }, error => res.status(500).send(error));
    }

    public atualizarHospedagem(req: Request, res: Response) {
        Hospedagem.update(req.body, {
            where: {
                id: req.body.id
            }
        }).then(hospedagem => {
            if (hospedagem != null && hospedagem == 1)
                Hospedagem.findOne({
                    where: {
                        id: req.body.id
                    }
                })
                .then(hosp => {
                    res.status(200).json(hosp);
                })
            else
                res.status(404).end();
        }, error => res.status(500).send(error));
    }

    public deletarHospedagem(req: Request, res: Response) {
        Hospedagem.delete(req.body, {
            where: {
                id: req.body.id
            }
        }).then(Hospedagem => {
            res.status(200).end();
        }, error => res.status(500).send(error));
    }

    public selecionarHospedagemPedinte(req: Request, res: Response) {
        sequelize.query("SELECT `hospedagems`.`id` AS `id`, `id_user_pedinte`, `id_user_hospedador`, `dataInicio`, \
            \n`dataFim`, `id_pets`, `status`, `perfis`.`primeiroNome` AS `primeiroNome`, \
            \n`perfis`.`ultimoNome` AS `ultimoNome`, `perfis`.`imagem` AS `imagem` \
            \nFROM `hospedagems` AS `hospedagems`, `perfis` AS `perfis` \
            \nWHERE `hospedagems`.`id_user_pedinte` = ? AND `perfis`.`id_user` = `hospedagems`.`id_user_hospedador`",
        {type: sequelize.QueryTypes.SELECT, replacements: [req.params.id_user_pedinte]})
        .then(hospedagens =>{            
            let array_list = hospedagens.map(hosp => {
                if (hosp.imagem != null)
                    hosp.imagem = "http://" + config.serverIP + ":" + config.serverPort + "/" + hosp.imagem.replace(/\\/gi, "/");

                return hosp;
            });

            res.status(200).json(array_list);
        }, error => res.status(500).send(error));
    }

    public selecionarHospedagemHospedador(req: Request, res: Response) {
        sequelize.query("SELECT `hospedagems`.`id` AS `id`, `id_user_pedinte`, `id_user_hospedador`, `dataInicio`, \
            \n`dataFim`, `id_pets`, `status`, `perfis`.`primeiroNome` AS `primeiroNome`, \
            \n`perfis`.`ultimoNome` AS `ultimoNome`, `perfis`.`imagem` AS `imagem` \
            \nFROM `hospedagems` AS `hospedagems`, `perfis` AS `perfis` \
            \nWHERE `hospedagems`.`id_user_hospedador` = ? AND `perfis`.`id_user` = `hospedagems`.`id_user_pedinte`",
        {type: sequelize.QueryTypes.SELECT, replacements: [req.params.id_user_hospedador]})
        .then(hospedagens => {
            
            let array_list = hospedagens.map(hosp => {
                if (hosp.imagem != null)
                    hosp.imagem = "http://" + config.serverIP + ":" + config.serverPort + "/" + hosp.imagem.replace(/\\/gi, "/");

                return hosp;
            });

            res.status(200).json(array_list);
        }, error => res.status(500).send(error));
    }

    public selecionarPetHospedagem(req: Request, res: Response) {
        sequelize.query("SELECT * FROM pets WHERE id_pet IN ("+ req.params.id_pets.replace(/;/gi, ",") +")",
        {type: sequelize.QueryTypes.SELECT})
        .then(pets => {
            res.status(200).json(pets);
        }, error => res.status(500).send(error));
    }

    public finalizarHospedagem(req: Request, res: Response) {
        Hospedagem.update(req.body, {
            where: {
                id: req.body.id
            }
        })
        .then(hosp => {
            if (req.body.comentario != null && req.body.comentario.trim() != "") {
                let data = {
                    comentario: req.body.comentario ,
                    remetente: req.body.id_user_pedinte,
                    destinatario: req.body.id_user_hospedador
                }

                Comment.create(data).then(comment => {
                    res.status(200).end();

                }, error => res.status(500).send(error));
            }
        }, error => res.status(500).send(error));
    }
}