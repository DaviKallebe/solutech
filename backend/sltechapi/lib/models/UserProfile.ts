import Sequelize from 'sequelize';
import { sequelize } from "../mysql";
import { User } from "./User";

export const UserProfile = sequelize.define('perfil', {
    primeiroNome: {
        type: Sequelize.STRING(20),
        allowNull: false
    },
    ultimoNome: {
        type: Sequelize.STRING(20),
        allowNull: false
    },
    documento: {
        type: Sequelize.STRING(20),
        allowNull: false
    },
    orgaoEmissor: {
        type: Sequelize.STRING(20),
        allowNull: false
    },
    cpf: {
        type: Sequelize.STRING(15),
        allowNull: false
    },
    nascimento: {
        type: Sequelize.DATE
    },
    telefone: {
        type: Sequelize.STRING(15)
    },
    descricao: {
        type: Sequelize.TEXT('tiny')
    },
    imagem: {
        type: Sequelize.STRING(255)
    }
});
