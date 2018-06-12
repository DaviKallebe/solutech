import Sequelize from 'sequelize';
import { sequelize } from "../mysql";
import { User } from "./User";

export const UserPlace = sequelize.define('usuario_lugar', {
    id_lugar: {
        type: Sequelize.BIGINT(10).UNSIGNED,
        autoIncrement: true,
		allowNull: false,
		primaryKey: true
    },
    descricao: {
        type: Sequelize.TEXT('tiny'),
        allowsNull: false
    },
    cep: {
        type: Sequelize.STRING(10)
    },
    logradouro: {
        type: Sequelize.STRING(40)
    },
    estado: {
        type: Sequelize.STRING(20)
    },
    cidade: {
        type: Sequelize.STRING(20)
    },
    bairro: {
        type: Sequelize.STRING(30)
    },
    numero: {
        type: Sequelize.INTEGER.UNSIGNED
    },
    complemento: {
        type: Sequelize.STRING(15)
    },
    latitude: {
        type: Sequelize.DECIMAL(10, 8)
    },
    longitude: {
        type: Sequelize.DECIMAL(11, 8)
    }
});
