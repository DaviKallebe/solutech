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
                        primeiroNome: user.perfi.primeiroNome,
                        ultimoNome: user.perfi.ultimoNome,
                        telefone: user.perfi.telefone
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
            req.body.nomeCompleto = req.body.primeiroNome + " " + req.body.ultimoNome;
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
                    imagem: profile.imagem != null ? "http://" + config.serverIP + ":" + config.serverPort + 
                                "/" + profile.imagem.replace(/\\/gi, "/") : profile.imagem
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
                        imagem: profile.imagem != null ? "http://" + config.serverIP + ":" + config.serverPort + 
                                    "/" + profile.imagem.replace(/\\/gi, "/") : profile.imagem
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
                            imagem: profile.imagem != null ? "http://" + config.serverIP + ":" + config.serverPort +
                                    "/" + profile.imagem.replace(/\\/gi, "/") : profile.imagem
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
            if (profile.imagem != null)
                profile.imagem = "http://" + config.serverIP + ":" + config.serverPort + "/" + profile.imagem.replace(/\\/gi, "/");

            res.status(200).json(profile);
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
            },
            individualHooks: true            
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
        .catch(error => {
            console.log(error);
            res.status(500).send(error)
        });
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

    public UserSearch(req: Request, res: Response) {
        if (req.query.nome == null || req.query.nome == "") {
            sequelize.query("SELECT `hospedadores`.`id`, `usuario`.`primeiroNome`, `usuario`.`ultimoNome`, `hospedadores`.`rg`, \
            \n`hospedadores`.`orgaoEmissor`, `hospedadores`.`nascimento`, `hospedadores`.`cpf`, `usuario`.`telefone`, \
            \n`hospedadores`.`cuidaIdoso`, `hospedadores`.`cuidaFilhote`, `hospedadores`.`cuidaFemea`, `hospedadores`.`cuidaMacho`, \
            \n`hospedadores`.`cuidaCastrado`, `hospedadores`.`cuidaPequeno`, `hospedadores`.`cuidaGrande`, `hospedadores`.`cuidaExotico`, \
            \n`hospedadores`.`cuidaCachorro`, `hospedadores`.`cuidaGato`, `hospedadores`.`cuidaMamifero`, `hospedadores`.`cuidaReptil`, \
            \n`hospedadores`.`cuidaAve`, `hospedadores`.`cuidaPeixe`, `hospedadores`.`likes`, `hospedadores`.`dislikes`, \
            \n`hospedadores`.`preferenciaAnimal`, `hospedadores`.`quantidadeAnimal`, `hospedadores`.`tipoSupervisao`, \
            \n`hospedadores`.`numeroComentario`, `hospedadores`.`totalLike`, `hospedadores`.`totalPLN`, `hospedadores`.`preco`, \
            \n`hospedadores`.`precoExotico`, `hospedadores`.`createdAt`, `hospedadores`.`updatedAt`, `hospedadores`.`id_user`,  \
            \n`hospedadores`.`usuarioNota`, `usuario`.`imagem` AS `imagem`, `usuario`.`descricao` AS `descricao` \
            \nFROM `hospedadores` AS `hospedadores` LEFT OUTER JOIN `perfis` AS `usuario` ON `hospedadores`.`id_user` = `usuario`.`id_user`;")
            .then(userHospedador => {
                let array_list = userHospedador[1].map(hosp => {
                    if (hosp.imagem != null)
                        hosp.imagem = "http://" + config.serverIP + ":" + config.serverPort + "/" + hosp.imagem.replace(/\\/gi, "/");

                    return hosp;
                });
                res.status(200).json(array_list);
            }, err => res.status(500).json(err));
        }
        else {
            sequelize.query("SELECT `hospedadores`.`id`, `usuario`.`primeiroNome`, `usuario`.`ultimoNome`, `hospedadores`.`rg`, \
            \n`hospedadores`.`orgaoEmissor`, `hospedadores`.`nascimento`, `hospedadores`.`cpf`, `usuario`.`telefone`, \
            \n`hospedadores`.`cuidaIdoso`, `hospedadores`.`cuidaFilhote`, `hospedadores`.`cuidaFemea`, `hospedadores`.`cuidaMacho`, \
            \n`hospedadores`.`cuidaCastrado`, `hospedadores`.`cuidaPequeno`, `hospedadores`.`cuidaGrande`, `hospedadores`.`cuidaExotico`, \
            \n`hospedadores`.`cuidaCachorro`, `hospedadores`.`cuidaGato`, `hospedadores`.`cuidaMamifero`, `hospedadores`.`cuidaReptil`, \
            \n`hospedadores`.`cuidaAve`, `hospedadores`.`cuidaPeixe`, `hospedadores`.`likes`, `hospedadores`.`dislikes`, \
            \n`hospedadores`.`preferenciaAnimal`, `hospedadores`.`quantidadeAnimal`, `hospedadores`.`tipoSupervisao`, \
            \n`hospedadores`.`numeroComentario`, `hospedadores`.`totalLike`, `hospedadores`.`totalPLN`, `hospedadores`.`preco`, \
            \n`hospedadores`.`precoExotico`, `hospedadores`.`createdAt`, `hospedadores`.`updatedAt`, `hospedadores`.`id_user`,  \
            \n`hospedadores`.`usuarioNota`, `usuario`.`imagem` AS `imagem`, `usuario`.`descricao` AS `descricao` \
            \nFROM `hospedadores` AS `hospedadores`, `perfis` AS `usuario` WHERE `hospedadores`.`id_user` = `usuario`.`id_user` AND MATCH(`usuario`.`nomeCompleto`) AGAINST(? IN BOOLEAN MODE);",
            {type: sequelize.QueryTypes.SELECT, replacements: ['+*' + req.query.nome + '*']})
            .then(hospedadores => {
                let array_list = hospedadores.map(hosp => {
                    if (hosp.imagem != null)
                        hosp.imagem = "http://" + config.serverIP + ":" + config.serverPort + "/" + hosp.imagem.replace(/\\/gi, "/");

                    return hosp;
                });
                res.status(200).json(array_list);
            }, err => res.status(500).json(err));
        }
    }

    public procurarHospedadorFiltro(req: Request, res: Response) {
        let keys = Object.keys(req.query).map(key => {
            if (key != "nomeCompleto" && key != "id_user")
                return "`hospedadores`.`" + key + "` = 1";
            if (key == "nomeCompleto")
                return "MATCH(`usuario`.`nomeCompleto`) AGAINST('+*" + req.query[key] + "*' IN BOOLEAN MODE)";
            if (key == "id_user")    
                return "`hospedadores`.`id_user` != " + req.query[key];
            return '';
        });

        sequelize.query("SELECT `hospedadores`.`id`, `usuario`.`primeiroNome`, `usuario`.`ultimoNome`, `hospedadores`.`rg`, \
        \n`hospedadores`.`orgaoEmissor`, `hospedadores`.`nascimento`, `hospedadores`.`cpf`, `usuario`.`telefone`, \
        \n`hospedadores`.`cuidaIdoso`, `hospedadores`.`cuidaFilhote`, `hospedadores`.`cuidaFemea`, `hospedadores`.`cuidaMacho`, \
        \n`hospedadores`.`cuidaCastrado`, `hospedadores`.`cuidaPequeno`, `hospedadores`.`cuidaGrande`, `hospedadores`.`cuidaExotico`, \
        \n`hospedadores`.`cuidaCachorro`, `hospedadores`.`cuidaGato`, `hospedadores`.`cuidaMamifero`, `hospedadores`.`cuidaReptil`, \
        \n`hospedadores`.`cuidaAve`, `hospedadores`.`cuidaPeixe`, `hospedadores`.`likes`, `hospedadores`.`dislikes`, \
        \n`hospedadores`.`preferenciaAnimal`, `hospedadores`.`quantidadeAnimal`, `hospedadores`.`tipoSupervisao`, \
        \n`hospedadores`.`numeroComentario`, `hospedadores`.`totalLike`, `hospedadores`.`totalPLN`, `hospedadores`.`preco`, \
        \n`hospedadores`.`precoExotico`, `hospedadores`.`createdAt`, `hospedadores`.`updatedAt`, `hospedadores`.`id_user`,  \
        \n`hospedadores`.`usuarioNota`, `usuario`.`imagem` AS `imagem`, `usuario`.`descricao` AS `descricao` \
        \nFROM `hospedadores` AS `hospedadores`, `perfis` AS `usuario` \
        \nWHERE `hospedadores`.`id_user` = `usuario`.`id_user` AND "+ keys.join(" AND ") +";",
        {type: sequelize.QueryTypes.SELECT})
        .then(hospedadores => {
            let array_list = hospedadores.map(hosp => {
                if (hosp.imagem != null)
                    hosp.imagem = "http://" + config.serverIP + ":" + config.serverPort + "/" + hosp.imagem.replace(/\\/gi, "/");

                return hosp;
            });
            res.status(200).json(array_list);
        }, err => res.status(500).json(err));
    }
}
