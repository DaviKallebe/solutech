import Sequelize from 'sequelize';
import { pbkdf2, pbkdf2Sync, randomBytes } from 'crypto';
import { sequelize } from "../mysql";
import { UserPlace } from "../models/UserPlace"
import { UserProfile } from "../models/UserProfile";

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
        type: Sequelize.STRING(30),
        unique: true
    },
    password: {
        type: Sequelize.STRING(512)
    },
    salt: {
        type: Sequelize.STRING(512)
    },
    facebook_id: {
        type: Sequelize.BIGINT.UNSIGNED,
        unique: true
    },
    facebook_email: {
        type: Sequelize.STRING(30),
        unique: true
    },
    account_creation: {
        type: Sequelize.INTEGER.UNSIGNED
    }
}, {
    hooks: {
        afterCreate: (user, options) => {
            if (user.password && user.password != ""){
                randomBytes(256, (err, buf) => {
                    if (err) throw err;

                    pbkdf2(user.password, buf.toString('hex'), 1, 256, 'sha512', (err, derivedKey) => {
                        if (err) throw err;
                        user.password = derivedKey.toString('hex');
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
    var pass = new Buffer(password, 'binary');
    return this.password === pbkdf2Sync(pass, this.salt, 1, 256, 'sha512').toString('hex');
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
});
