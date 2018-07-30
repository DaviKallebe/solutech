import { Request, Response } from 'express';
import { Hospedagem } from "../models/Hospedagem";

export class HospedagemController {
    public novaHospedagem(req: Request, res: Response) {
        Hospedagem.create(req.body).then(hospedagem => {
            res.status(200).json(hospedagem);
        }, error => res.status(500).send(error));
    }

    public atualizarHospedagem(req: Request, res: Response) {
        Hospedagem.update(req.body).then(hospedagem => {
            if (hospedagem != null)
                res.status(200).json(hospedagem);
            else
                res.status(404).end();
        }, error => res.status(500).send(error));
    }

    public deletarHospedagem(req: Request, res: Response) {
        Hospedagem.delete(req.body).then(Hospedagem => {
            res.status(200).end();
        }, error => res.status(500).send(error));
    }

    public selecionarHospedagemPedinte(req: Request, res: Response) {
        Hospedagem.findAll({
            where: {
                id_user_pedinte: req.params.id_user_pedinte
            }
        }).then(hospedagens =>{
            res.status(200).json(hospedagens);
        }, error => res.status(500).send(error));
    }

    public selecionarHospedagemHospedador(req: Request, res: Response) {
        Hospedagem.findAll({
            where: {
                id_user_hospedador: req.params.id_user_hospedador
            }
        }).then(hospedagens => {
            res.status(200).json(hospedagens);
        }, error => res.status(500).send(error));
    }
}