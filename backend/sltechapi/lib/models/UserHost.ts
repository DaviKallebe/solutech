import * as Sequelize from 'sequelize';
import { sequelize } from "../mysql";
import { User } from "./User";

export const UserHost = sequelize.define('hospedador', {
    likes: {
        type: Sequelize.INTEGER.UNSIGNED,
        defaultValue: 0
    },
    dislikes: {
        type: Sequelize.INTEGER.UNSIGNED,
        defaultValue: 0
    },
    perferenciaAnimal: {
        type: Sequelize.STRING
    },
    quantidadeAnimal: {
        type: Sequelize.STRING
    },
    tipoSupervisao: {
        type: Sequelize.INTEGER
    },
    filhotes: {
        type: Sequelize.BOOLEAN
    },
    idosos: {
        type: Sequelize.BOOLEAN
    },
    macho: {
        type: Sequelize.BOOLEAN
    },
    femea: {
        type: Sequelize.BOOLEAN
    }
});
