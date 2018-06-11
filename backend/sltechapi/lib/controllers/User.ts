import { User } from "../models/User";
import { Request, Response } from 'express';

export class UserController {
    public loginNormal(req: Request, res: Response) {
        User.findOne({
            where: {
                username: req.query.username
            }
        }).then(user => {
            if (user && user.checkPassword(req.query.password)){
                res.json(user);
            } else
                return res.send("Incorrect login");
        }, err => {
            res.send(err);
        });
    }

    public createUserNormal(req: Request, res: Response) {
        User.create(req.body).then(() => {
            res.send('User created');
        }, err => {
            res.send(err);
        });
    }
}
