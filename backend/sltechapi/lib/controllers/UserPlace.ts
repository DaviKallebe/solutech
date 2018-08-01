import { UserPlace } from "../models/UserPlace"
import { Request, Response } from 'express';

export class UserPlaceController {
    public setPlace(req: Request, res: Response) {
        UserPlace.create(req.body).then(place => {
            res.json(place);
        }, err => {
            res.send(err);
        });
    }

    public getPlace(req: Request, res: Response) {
        UserPlace.findOne({
            where: {
                id_user: req.params.id_user
            }
        }).then(place => {
            res.json(place);
        }, err => {
            res.send(err);
        })
    }

    public updatePlace(req: Request, res: Response) {
        UserPlace.update(req.body, {
            where: {
                id_user: req.params.id_user
            }
        }).then(place =>
            res.json(place)
        ).catch(err =>
            res.send(err)
        );
    }
    
    public selecionarLogradouro(req: Request, res: Response) {
        UserPlace.findOne({
            where: {
                id_user: req.params.id_user
            }
        })
        .then(place => {
            if (place != null)
                res.status(200).json(place);
            else
                res.status(404).end();
        }, error => res.status(500).send(error));
    }

    public atualizarLogradouro(req: Request, res: Response) {
        UserPlace.update(req.body, {
            where: {
                id_lugar: req.body.id_lugar
            }
        })
        .then(place => {
            res.status(200).json(place);
        }, error => res.status(500).send(error));
    }

    public criarLogradouro(req: Request, res: Response) {
        UserPlace.create(req.body).then(place => {
            res.status(200).json(place);
        }, error => res.status(500).send(error));
    }
}
