import Sequelize from 'sequelize';
import { sequelize } from "../mysql";
import { User } from "./User";

export const UserPlace = sequelize.define('logradouro', {
    id_lugar: {
        type: Sequelize.BIGINT(10).UNSIGNED,
        autoIncrement: true,
		allowNull: false,
		primaryKey: true
    },
    cep: {
        type: Sequelize.STRING(10)
    },
    rua: {
        type: Sequelize.STRING(30)
    },
    estado: {
        type: Sequelize.STRING(20)
    },
    cidade: {
        type: Sequelize.STRING(30)
    },
    bairro: {
        type: Sequelize.STRING(15)
    },
    numero: {
        type: Sequelize.INTEGER.UNSIGNED
    },
    complemento: {
        type: Sequelize.STRING(15)
    },
    logradouro: {
        type: Sequelize.STRING(40)
    },
    latitude: {
        type: Sequelize.DECIMAL(10, 8)
    },
    longitude: {
        type: Sequelize.DECIMAL(11, 8)
    },
    descricao: {
        type: Sequelize.TEXT("tiny"),
        allowsNull: false
    },
    imagem: {
        type: Sequelize.TEXT("tiny"),
    }
});
