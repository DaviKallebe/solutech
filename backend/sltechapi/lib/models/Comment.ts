import * as Sequelize from 'sequelize';
import { sequelize } from "../mysql";

export const Comment = sequelize.define('comentarios', {
    remetente: {
        type: Sequelize.BIGINT.UNSIGNED,
		allowNull: false
    },
    destinatario: {
        type: Sequelize.BIGINT.UNSIGNED,
		allowNull: false
    },
    comentario: {
        type: Sequelize.TEXT('tiny')
    },
    ponto: {
        type: Sequelize.DOUBLE
    }
});
