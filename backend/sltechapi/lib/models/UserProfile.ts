import * as Sequelize from 'sequelize';
import { sequelize } from "../mysql";
import { User } from "./User";

export const UserProfile = sequelize.define('perfil', {
    primeiroNome: {
        type: Sequelize.STRING(20),
        allowNull: false
    },
    ultimoNome: {
        type: Sequelize.STRING(40),
        allowNull: false
    },
    documento: {
        type: Sequelize.STRING(20),
        allowNull: true
    },
    orgaoEmissor: {
        type: Sequelize.STRING(20),
        allowNull: true
    },
    cpf: {
        type: Sequelize.STRING(15),
        allowNull: true
    },
    nascimento: {
        type: Sequelize.DATE,
        allowNull: true
    },
    telefone: {
        type: Sequelize.STRING(15),
        allowNull: true
    },
    descricao: {
        type: Sequelize.TEXT('tiny'),
        allowNull: true
    },
    imagem: {
        type: Sequelize.STRING(255),
        allowNull: true
    }
});
