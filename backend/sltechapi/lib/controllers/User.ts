import { User } from "../models/User";
import { UserProfile } from "../models/UserProfile"
import { UserPlace } from "../models/UserPlace"
import { Request, Response } from 'express';

export class UserController {
    public loginNormal(req: Request, res: Response) {
        User.findOne({
            attributes: ['email', 'id_user', 'salt', 'pword'],
            where: {
                email: req.query.email
            },
            include: [{
                model: UserProfile,
                attributes: ['primeiroNome', 'ultimoNome', 'telefone']
            }]
        }).then(user => {
            if (user && user.checkPassword(req.query.pword)){
                let result = {
                    email: user.email,
                    id_user: user.id_user,
                    primeiroNome: user.perfil.primeiroNome,
                    ultimoNome: user.perfil.ultimoNome,
                    telefone: user.perfil.telefone/*,
                    nascimento: user.perfil.nascimento,
                    documento: user.perfil.documento,
                    orgaoEmissor: user.perfil.orgaoEmissor,
                    cpf: user.perfil.orgaoEmissor,
                    descricao: user.perfil.descricao,
                    imagem: user.perfil.imagem*/
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
                    primeiroNome: profile.primeiroNome,
                    ultimoNome: profile.ultimoNome,
                    nascimento: profile.nascimento,
                    documento: profile.documento,
                    orgaoEmissor: profile.orgaoEmissor,
                    cpf: profile.orgaoEmissor,
                    telefone: profile.telefone,
                    descricao: profile.descricao,
                    imagem: profile.imagem
                }
                res.status(200).json(result);
            }, err => {
                res.status(500).send(err);
            });
        }, err => {
            res.status(500).send(err);
        });
    }

    public loginFacebook(req: Request, res: Response) {
        User.findOne({
            where: {
                facebookId: req.query.facebookId
            }
        }).then(user => {
            if (user) res.status(200).json(user);
        }, err => {
            res.status(500).send(err);
        });
    }

    public listUsers(req: Request, res: Response) {
        UserProfile.findAll({
        }).then(profile => {
            res.status(200).send(profile);
        }, err => {
            res.status(500).send(err);
        });
    }

    public generateHashes(req: Request, res: Response) {
        User.findAll({
            where: {
                salt: null
            }
        }).then(user_list => {
            user_list.forEach(user => {
                user.generateHash();
            });
            res.status(200).send(true);
        }, err => {
            res.status(500).send(err);
        })
    }
}
