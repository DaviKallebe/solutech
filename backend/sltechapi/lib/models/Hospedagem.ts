import * as Sequelize from 'sequelize';
import { sequelize } from "../mysql";

export const Hospedagem = sequelize.define('hospedagems', {
    id_user_pedinte: {
        type: Sequelize.BIGINT
    },
    id_user_hospedador: {
        type: Sequelize.BIGINT
    },
    dataInicio: {
        type: Sequelize.STRING(15)
    },
    dataFim: {
        type: Sequelize.STRING(15)
    },
    id_pets: {
        type: Sequelize.STRING(50)
    },
    status: {
        type: Sequelize.INTEGER
    },
    nota: {
        type: Sequelize.DOUBLE
    }
});

Hospedagem.sync({force: false}).then(() => {
    //Table created
});