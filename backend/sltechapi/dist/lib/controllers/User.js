"use strict";
const User_1 = require("../models/User");
const UserProfile_1 = require("../models/UserProfile");
class UserController {
    loginNormal(req, res) {
        User_1.User.findOne({
            attributes: ['email', 'id_user', 'salt', 'pword'],
            where: {
                email: req.query.email
            },
            include: [{
                    model: UserProfile_1.UserProfile,
                    attributes: ['primeiroNome', 'ultimoNome', 'telefone']
                }]
        }).then(user => {
            if (user && user.checkPassword(req.query.pword)) {
                let result = {
                    email: user.email,
                    id_user: user.id_user,
                    primeiroNome: user.perfil.primeiroNome,
                    ultimoNome: user.perfil.ultimoNome,
                    telefone: user.perfil.telefone
                };
                res.status(200).json(result);
            }
            else
                res.status(401).json(false);
        });
    }
    createUserNormal(req, res) {
        User_1.User.create(req.body).then(user => {
            let params = req.body;
            params.id_user = user.id_user;
            UserProfile_1.UserProfile.create(params).then(profile => {
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
                };
                res.status(200).json(result);
            }, err => {
                res.status(500).send(err);
            });
        }, err => {
            res.status(500).send(err);
        });
    }
    loginFacebook(req, res) {
        User_1.User.findOne({
            where: {
                facebookId: req.query.facebookId
            }
        }).then(user => {
            if (user)
                res.status(200).json(user);
        }, err => {
            res.status(500).send(err);
        });
    }
    listUsers(req, res) {
        UserProfile_1.UserProfile.findAll({}).then(profile => {
            res.status(200).send(profile);
        }, err => {
            res.status(500).send(err);
        });
    }
    generateHashes(req, res) {
        User_1.User.findAll({
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
        });
    }
}
exports.UserController = UserController;
//# sourceMappingURL=User.js.map