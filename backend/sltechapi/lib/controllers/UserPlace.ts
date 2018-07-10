import { User } from "../models/User";
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
    /*
    public existPlace(req: Request, res: Response) {
        UserPlace.findOne({
            where: {
                id_user: req.params.id_user
            }
        }).then(place => {
            if (place){
                place.found = true;
                res.json(place);
            } else {
                let result = {found: false};
                res.json(result);
            }
        })
    }*/
}
