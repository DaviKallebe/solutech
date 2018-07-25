import * as Sequelize from 'sequelize';
import { sequelize } from "../mysql";
import { User } from "./User";

export const UserHost = sequelize.define('hospedadores', {
    primeiroNome: {
        type: Sequelize.STRING
    },
    ultimoNome: {
        type: Sequelize.STRING
    },
    rg: {
        type: Sequelize.STRING
    },
    orgaoEmissor: {
        type: Sequelize.STRING
    },
    nascimento: {
        type: Sequelize.STRING
    },
    cpf: {
        type: Sequelize.STRING
    },
    telefone: {
        type: Sequelize.STRING
    },
    cuidaIdoso: {
        type: Sequelize.BOOLEAN
    },
    cuidaFilhote: {
        type: Sequelize.BOOLEAN
    },
    cuidaFemea: {
        type: Sequelize.BOOLEAN
    },
    cuidaMacho: {
        type: Sequelize.BOOLEAN
    },
    cuidaCastrado: {
        type: Sequelize.BOOLEAN
    },
    cuidaPequeno: {
        type: Sequelize.BOOLEAN
    },
    cuidaGrande: {
        type: Sequelize.BOOLEAN
    },
    cuidaExotico: {
        type: Sequelize.BOOLEAN
    },
    cuidaCachorro: {
        type: Sequelize.BOOLEAN
    },
    cuidaGatos: {
        type: Sequelize.BOOLEAN
    },
    cuidaMamifero: {
        type: Sequelize.BOOLEAN
    },
    cuidaReptil: {
        type: Sequelize.BOOLEAN
    },
    cuidaAve: {
        type: Sequelize.BOOLEAN
    },
    cuidaPeixe: {
        type: Sequelize.BOOLEAN
    },
    likes: {
        type: Sequelize.INTEGER,
        defaultValue: 0
    },
    dislikes: {
        type: Sequelize.INTEGER,
        defaultValue: 0
    },
    preferenciaAnimal: {
        type: Sequelize.STRING
    },
    quantidadeAnimal: {
        type: Sequelize.INTEGER
    },
    tipoSupervisao: {
        type: Sequelize.INTEGER
    },
    numerComentario: {
        type: Sequelize.INTEGER
    },
    totalLike: {
        type: Sequelize.INTEGER
    },     
    totalPLN: {
        type: Sequelize.DOUBLE
    },
    preco: {
        type: Sequelize.DOUBLE
    },
    precoExotico: {
        type: Sequelize.DOUBLE
    },
    descricao: {
        type: Sequelize.TEXT('medium')
    }
});
