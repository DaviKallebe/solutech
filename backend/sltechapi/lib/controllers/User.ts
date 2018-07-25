import { User } from "../models/User";
import { UserProfile } from "../models/UserProfile";
import { UserHost } from "../models/UserHost";
import { Pet } from "../models/Pet";
import { Request, Response } from 'express';
import { config } from '../../config';
import { sequelize } from "../mysql";


export class UserController {
    public errorHandler(error, req: Request, res: Response) {
        res.status(500).send(error);
    }

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
                    attributes: ['primeiroNome', 'ultimoNome', 'telefone'],
                }]
            }).then(user => {
                if (user) {
                    let result = {
                        email: user.email,
                        id_user: user.id_user,
                        primeiroNome: user.perfi.primeiroNome,
                        ultimoNome: user.perfi.ultimoNome,
                        telefone: user.perfi.telefone
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

    public getUsersByName = (req: Request, res: Response) => {
        sequelize.query("SELECT * FROM perfis WHERE primeiroNome like ? OR ultimoNome like ?", 
        {model: User, replacements: [ '%' +req.params.nome + '%', '%' + req.params.nome + '%'], type: sequelize.QueryTypes.SELECT})
        .then(users => {
            res.status(200).json(users);
        }, err => {
            res.status(500).json(err);
        });
        //.catch(error => this.errorHandler(error, req, res));

    }

    public generateHashes(req: Request, res: Response) {
        User.findAll({
            where: {
                salt: null
            }
        }).then(user_list => {
            user_list.forEach(user => {
                //user.generateHash();
            });
            res.status(200).send(true);
        }, err => {
            res.status(500).send(err);
        })
    }

    public getUserProfile(req: Request, res: Response) {        
        UserProfile.findOne({
            where: { 
                id_user: req.params.id_user
            }
        })
        .then(profile => {
            if (profile){                
                if (profile.imagem != null)
                    profile.imagem = "http://" + config.serverIP + ":" + config.serverPort + "/" + profile.imagem.replace(/\\/gi, "/");
                if (profile.updatedAt == req.query.updatedAt)
                    res.status(200).json(profile);
                else
                    res.status(200).json(profile);
            } else
                res.status(404).end();
        })
        .catch(error => this.errorHandler(error, req, res));
    }

    public updateUserProfile(req: Request, res: Response) {
        let data = req.body;

        if (req.file)
            data.imagem = req.file.path;

        UserProfile.update(data, {
            where: {
                id_user: req.body.id_user
            }
        })
        .then(profile => {
            UserProfile.findOne({
                where: {
                    id_user: req.body.id_user
                }
            }).then(newProfile => {
                if (newProfile.imagem != null)
                    newProfile.imagem = "http://" + config.serverIP + ":" + config.serverPort + "/" + newProfile.imagem.replace(/\\/gi, "/");
                res.status(200).json(newProfile);
            });
        })
        .catch(error => this.errorHandler(error, req, res));
    }

    public createPet = (req: Request, res: Response) => {
        Pet.create(req.body).then(pet => {
            res.status(200).json(pet);
        })
        .catch(error => this.errorHandler(error, req, res));
    }

    public updatePet(req: Request, res: Response) {
        Pet.update(req.body, {
            where: {
                id_user: req.body.id_user
            }
        })
        .then(pet => {
            res.status(200).json(pet);
        })
        .catch(error => this.errorHandler(error, req, res));
    }

    public getPet(req: Request, res: Response) {
        Pet.findOne({
            where: {
                id_pet: req.params.id_pet
            }
        })
        .then(pet => {
            if (pet)
                res.status(200).json(pet);
            else
                res.status(404).end();
        })
        .catch(error => this.errorHandler(error, req, res));
    }

    public getPetList(req: Request, res: Response) {
        Pet.findAll({
            where: {
                id_user: req.params.id_user
            }
        })
        .then(pets => {
            res.status(200).json(pets);
        })
        .catch(error => this.errorHandler(error, req, res));
    } 
    
    public getHospedador(req: Request, res: Response) {
        UserHost.findOne({
            where: {
                id_user: req.params.id_user
            }
        })
        .then(host => {
            if (host)
                res.status(200).json(host);
            else
                res.status(200).end();
        })
        .catch(error => res.status(500).send(error));
    }

    public updateHospedador(req: Request, res: Response) {
        UserHost.update({
            where: {
                id_user: req.body.id_user
            }
        })
        .then(host => {
            res.status(200).json(host);
        })
        .catch(err => res.status(500).send(err));
    }

    public createHospedador(req: Request, res: Response) {
        UserHost.create(req.body).then(host => {
            res.status(200).json(host);
        })
        .catch(err => res.status(500).json(err));
    }
}
