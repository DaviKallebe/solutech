import * as Sequelize from 'sequelize';
import { pbkdf2, pbkdf2Sync, randomBytes } from 'crypto';
import { sequelize } from "../mysql";
import { UserPlace } from "../models/UserPlace"
import { UserProfile } from "../models/UserProfile";
import { UserHost } from "../models/UserHost";
import { Comment } from "../models/Comment";
import { Message } from "../models/Message";
import { Pet } from "../models/Pet";

export const User = sequelize.define('usuario', {
    id_user: {
        type: Sequelize.BIGINT.UNSIGNED,
        autoIncrement: true,
		allowNull: false,
		primaryKey: true
    },
    username: {
        type: Sequelize.STRING(20),
        unique: true
    },
    email: {
        type: Sequelize.STRING(255),
        unique: true,
        allowNull: false
    },
    pword: {
        type: Sequelize.STRING(512)
    },
    salt: {
        type: Sequelize.STRING(512)
    },
    firebaseUid: {
        type: Sequelize.STRING(50),
        unique: true
    },
    account_creation: {
        type: Sequelize.INTEGER.UNSIGNED
    }
});

User.addHook('afterCreate', 'generateHash', (userInfo, option) => {
    if (userInfo.pword && userInfo.pword != ""){
        randomBytes(256, (err, buf) => {
            if (err) throw err;

            pbkdf2(userInfo.pword, buf.toString('hex'), 1, 256, 'sha512', (err, derivedKey) => {
                if (err) throw err;
                userInfo.pword = derivedKey.toString('hex');
                userInfo.account_creation = 1;
                userInfo.salt = buf.toString('hex');
                userInfo.save();
            });
        });
    }
    else {
        userInfo.account_creation = 2;
        userInfo.save();
    }
})

/*
User.prototype.checkPassword = function (password) {
    var pass = new Buffer(password, 'binary');
    return this.pword === pbkdf2Sync(pass, this.salt, 1, 256, 'sha512').toString('hex');
}*/

User.sync({force: false}).then(() => {
    //Table created
    User.hasOne(UserPlace, {foreignKeyConstraint: true, foreignKey: 'id_user'});
    UserPlace.belongsTo(User, {foreignKeyConstraint: true, foreignKey: 'id_user'});

    UserPlace.sync({force: false}).then(() => {
        //Table created
    });
    //
    User.hasOne(UserProfile, {foreignKeyConstraint: true, foreignKey: 'id_user'});
    UserProfile.belongsTo(User, {foreignKeyConstraint: true, foreignKey: 'id_user'});

    UserProfile.sync({force: false}).then(() => {
        //Table created
    });
    //
    User.hasOne(UserHost, {foreignKeyConstraint: true, foreignKey: 'id_user'});
    UserHost.belongsTo(User, {foreignKeyConstraint: true, foreignKey: 'id_user'});

    UserHost.sync({force: false}).then(() => {
        //Table created
    });
    //
    User.hasMany(Pet, {foreignKeyConstraint: true, foreignKey: 'id_user'});
    Pet.belongsTo(User, {foreignKeyConstraint: true, foreignKey: 'id_user'});

    Pet.sync({force: false}).then(() => {
        //Table created
    });
    /*
    User.hasMany(Comment, {foreignKeyConstraint: true, foreignKey: 'id_user'});
    Comment.belongsTo(User, {foreignKeyConstraint: true, foreignKey: 'destinatario'});

    User.hasMany(Comment, {foreignKeyConstraint: true, foreignKey: 'id_user'});
    Comment.belongsTo(User, {foreignKeyConstraint: true, foreignKey: 'remetente'});*/

    Comment.sync({force: false}).then(() => {
        //Table created
    });

    Message.sync({force: false}).then(() => {
        //Table created
    });
});
