import Sequelize from 'sequelize';
import { pbkdf2, pbkdf2Sync, randomBytes } from 'crypto';
import { sequelize } from "../mysql";

export const User = sequelize.define('user', {
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
    return this.password === pbkdf2Sync(password, this.salt, 1, 256, 'sha512').toString('hex');
}

User.sync({force: true}).then(() => {
    //Table created
});
