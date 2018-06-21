import Sequelize from 'sequelize';
import { pbkdf2, pbkdf2Sync, randomBytes } from 'crypto';
import { sequelize } from "../mysql";
import { UserPlace } from "../models/UserPlace"
import { UserProfile } from "../models/UserProfile";
import { UserHost } from "../models/UserHost";
import { Comment } from "../models/Comment"
import { Pet } from "../models/Pet";

export const User = sequelize.define('usuario', {
    id_user: {
        type: Sequelize.BIGINT(10).UNSIGNED,
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
        unique: true
    },
    pword: {
        type: Sequelize.STRING(512)
    },
    salt: {
        type: Sequelize.STRING(512)
    },
    facebookId: {
        type: Sequelize.BIGINT.UNSIGNED,
        unique: true
    },
    account_creation: {
        type: Sequelize.INTEGER.UNSIGNED
    }
}, {
    hooks: {
        afterCreate: (user, options) => {
            if (user.pword && user.pword != ""){
                randomBytes(256, (err, buf) => {
                    if (err) throw err;

                    pbkdf2(user.pword, buf.toString('hex'), 1, 256, 'sha512', (err, derivedKey) => {
                        if (err) throw err;
                        user.pword = derivedKey.toString('hex');
                        user.account_creation = 1;
                        user.salt = buf.toString('hex');
                        user.save();
                    });
                });
            }
            else {
                user.account_creation = 2;
                user.save();
            }
        }
    }
});

User.prototype.checkPassword = function (password) {
    console.log('password ' + password)
    var pass = new Buffer(password, 'binary');
    return this.pword === pbkdf2Sync(pass, this.salt, 1, 256, 'sha512').toString('hex');
}

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
});
