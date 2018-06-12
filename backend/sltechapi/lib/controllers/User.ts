import { User } from "../models/User";
import { UserProfile } from "../models/UserProfile"
import { Request, Response } from 'express';

export class UserController {
    public loginNormal(req: Request, res: Response) {
        User.findOne({
            attributes: ['email', 'id_user', 'salt', 'password'],
            where: {
                email: req.query.email
            },
            include: [{
                model: UserProfile,
                attributes: ['nome', 'idade', 'telefone', 'descricao', 'imagem']
            }]
        }).then(user => {
            if (user && user.checkPassword(req.query.password)){
                let result = {
                    email: user.email,
                    id_user: user.id_user,
                    nome: user.usuario_perfil.nome,
                    idade: user.usuario_perfil.idade,
                    telefone: user.usuario_perfil.telefone,
                    descricao: user.usuario_perfil.descricao,
                    imagem: user.usuario_perfil.imagem
                }

                res.status(200).json(result);
            } else
                res.status(401).json(false);
        })
    }

    public createUserNormal(req: Request, res: Response) {
        User.create(req.body).then(user => {
            let params = req.body;
            params.id_user = user.id_user;

            UserProfile.create(params).then(profile => {
                let result = {
                    email: user.email,
                    id_user: user.id_user,
                    nome: profile.nome,
                    idade: profile.idade,
                    telefone: profile.telefone,
                    descricao: profile.descricao,
                    imagem: profile.imagem
                }
                res.status(200).json(result);
            }, err => {
                res.status(500).send(err);
            });
        }, err => {
            res.send(err);
        });
    }

    public loginFacebook(req: Request, res: Response) {
        User.findOne({
            where: {
                facebook_id: req.query.facebook_id
            }
        }).then(user => {
            if (user) res.json(user);
        }, err => {
            res.send(err);
        });
    }
}
