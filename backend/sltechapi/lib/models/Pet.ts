import Sequelize from 'sequelize';
import { sequelize } from "../mysql";
import { User } from "./User";

export const Pet = sequelize.define('pet', {
    id_pet: {
        type: Sequelize.BIGINT(10).UNSIGNED,
        autoIncrement: true,
		allowNull: false,
		primaryKey: true
    },
    nome: {
        type: Sequelize.STRING(20)
    },
    idade: {
        type: Sequelize.INTEGER
    },
    especie: {
        type: Sequelize.INTEGER
    },
    raca: {
        type: Sequelize.STRING(20)
    },
    tamanho: {
        type: Sequelize.DECIMAL
    },
    peso: {
        type: Sequelize.DECIMAL
    },
    sexo: {
        type: Sequelize.INTEGER
    },
    vacinado: {
        type: Sequelize.BOOLEAN
    },
    castrado: {
        type: Sequelize.BOOLEAN
    },
    imagem: {
        type: Sequelize.TEXT("tiny")
    },
    outros: {
        type: Sequelize.TEXT("tiny")
    },
});
