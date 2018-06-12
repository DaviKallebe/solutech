import Sequelize from 'sequelize';
import { sequelize } from "../mysql";
import { User } from "./User";

export const UserProfile = sequelize.define('usuario_perfil', {
    nome: {
        type: Sequelize.STRING(20),
        allowNull: false
    },
    idade: {
        type: Sequelize.INTEGER.UNSIGNED
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
