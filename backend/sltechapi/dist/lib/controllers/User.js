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
                    attributes: ['nome', 'idade', 'telefone', 'descricao', 'imagem']
                }]
        }).then(user => {
            if (user && user.checkPassword(req.query.password)) {
                let result = {
                    email: user.email,
                    id_user: user.id_user,
                    nome: user.usuario_perfil.nome,
                    idade: user.usuario_perfil.idade,
                    telefone: user.usuario_perfil.telefone,
                    descricao: user.usuario_perfil.descricao,
                    imagem: user.usuario_perfil.imagem
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
                    nome: profile.nome,
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
}
exports.UserController = UserController;
//# sourceMappingURL=User.js.map