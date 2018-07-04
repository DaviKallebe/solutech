import { User } from "../models/User";
import { UserProfile } from "../models/UserProfile"
import { UserPlace } from "../models/UserPlace"
import { Request, Response } from 'express';

export class UserController {
    public createUser(req: Request, res: Response) {
        let data = req.body;
        data.firebaseUid = req.query.firebaseUid

        User.create(data).then(user => {
            let params = req.body;
            params.id_user = user.id_user;

            UserProfile.create(params).then(profile => {
                let result = {
                    email: user.email,
                    id_user: user.id_user,
                    primeiroNome: profile.primeiroNome,
                    ultimoNome: profile.ultimoNome
                }
                res.status(200).json(result)
            }, err => {
                res.status(500).json(err);
            });
        }, err => {
            res.status(500).json(err);
        });
    }

    public findOneUser = (firebaseUid) => {
        User.findOne({
            attributes: ['email', 'id_user'],
            where: {
                firebaseUid: firebaseUid
            },
            include: [{
                model: UserProfile,
                attributes: ['primeiroNome', 'ultimoNome', 'telefone']
            }]
        }).then(user => {
            if (user) {
                let result = {
                    email: user.email,
                    id_user: user.id_user,
                    primeiroNome: user.perfil.primeiroNome,
                    ultimoNome: user.perfil.ultimoNome,
                    telefone: user.perfil.telefone
                }

                return <any>result;
            }
            else
                return null;
        }, err => { return <any>null;});
    }

    public loginWithFirebase = (req: Request, res: Response) => {
        if (req.query.firebaseUid) {
            User.findOne({
                attributes: ['email', 'id_user'],
                where: {
                    firebaseUid: req.query.firebaseUid
                },
                include: [{
                    model: UserProfile,
                    attributes: ['primeiroNome', 'ultimoNome', 'telefone']
                }]
            }).then(user => {
                if (user) {
                    let result = {
                        email: user.email,
                        id_user: user.id_user,
                        primeiroNome: user.perfil.primeiroNome,
                        ultimoNome: user.perfil.ultimoNome,
                        telefone: user.perfil.telefone
                    }

                    res.status(200).json(result);
                }
                else
                    res.status(401).end();
            }, err => {res.status(500).json(err);});
        }
        else
            res.status(400).end();
    }

    public createUserWithFirebase = (req: Request, res: Response) => {
        if (req.body.email && req.body.primeiroNome && req.body.ultimoNome && req.query.firebaseUid) {
            User.findOne({
                attributes: ['email', 'id_user'],
                where: {
                    firebaseUid: req.query.firebaseUid
                },
                include: [{
                    model: UserProfile,
                    attributes: ['primeiroNome', 'ultimoNome', 'telefone']
                }]
            }).then(user => {

                if (user) {
                    let result = {
                        email: user.email,
                        id_user: user.id_user,
                        primeiroNome: user.perfil.primeiroNome,
                        ultimoNome: user.perfil.ultimoNome,
                        telefone: user.perfil.telefone
                    }
                    res.status(200).json(result);
                }
                else
                    this.createUser(req, res);
            }, err => {res.status(500).json(err);});
        } else
            res.status(400).end();
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
                res.status(201).json(result);
            }, err => {
                res.status(500).send(err);
            });
        }, err => {
            res.status(500).send(err);
        });
    }

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
                    telefone: user.perfil.telefone
                }

                res.status(200).json(result);
            } else
                res.status(401).json(false);
        })
    }

    public createUserFacebook(req: Request, res: Response) {
        User.findOne({
            where : {
                facebookId: req.body.facebookId
            }
        }).then(user => {
            if (user) {
                UserProfile.findOne({
                    where: {
                        id_user: user.id_user
                    }
                }).then(profile => {
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
                }, err => {res.status(500).send(err)});
            }
            else {
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
                        res.status(201).json(result);
                    }, err => {
                        res.status(500).send(err);
                    });
                }, err => {res.status(500).send(err);});
            }
        }, err => {res.status(500).send(err);});
    }

    public loginFacebook(req: Request, res: Response) {
        User.findOne({
            where: {
                facebookId: req.query.facebookId
            }
        }).then(user => {
            if (user) {
                let result = {
                    email: user.email,
                    id_user: user.id_user,
                    primeiroNome: user.perfil.primeiroNome,
                    ultimoNome: user.perfil.ultimoNome
                }
                res.status(200).json(user);
            }
            else
                res.status(401).json(false);
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
