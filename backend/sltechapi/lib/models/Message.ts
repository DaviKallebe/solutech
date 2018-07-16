import * as Sequelize from 'sequelize';
import { sequelize } from "../mysql";

export const Message = sequelize.define('mensagens', {
    remetente: {
        type: Sequelize.BIGINT.UNSIGNED,
		allowNull: false
    },
    destinatario: {
        type: Sequelize.BIGINT.UNSIGNED,
		allowNull: false
    },
    mensagem: {
        type: Sequelize.TEXT('tiny'),
        allowNull: false
    },
    data: {
        type: Sequelize.DATE,
        allowNull: false
    }
});
