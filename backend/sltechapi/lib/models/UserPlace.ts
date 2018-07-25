import * as Sequelize from 'sequelize';
import { sequelize } from "../mysql";
import { User } from "./User";

export const UserPlace = sequelize.define('logradouros', {
    id_lugar: {
        type: Sequelize.BIGINT.UNSIGNED,
        autoIncrement: true,
		allowNull: false,
		primaryKey: true
    },
    cep: {
        type: Sequelize.STRING(10)
    },
    rua: {
        type: Sequelize.STRING(50)
    },
    estado: {
        type: Sequelize.STRING(25)
    },
    cidade: {
        type: Sequelize.STRING(40)
    },
    bairro: {
        type: Sequelize.STRING(25)
    },
    numero: {
        type: Sequelize.INTEGER.UNSIGNED
    },
    complemento: {
        type: Sequelize.STRING(30)
    },
    logradouro: {
        type: Sequelize.STRING(100)
    },
    latitude: {
        type: Sequelize.DECIMAL(10, 8)
    },
    longitude: {
        type: Sequelize.DECIMAL(11, 8)
    },
    descricao: {
        type: Sequelize.TEXT("medium"),
        allowNull: false
    },
    imagem: {
        type: Sequelize.TEXT("long"),
    }
});
