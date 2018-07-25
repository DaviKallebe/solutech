import {config} from "../config";
import * as Sequelize from 'sequelize';

export let sequelize = new Sequelize.Sequelize(
	config.development.database,
	config.development.username,
	config.development.password,
	{
		host: config.development.host,
		dialect: "mysql",
		operatorsAliases: false
	}
)

if (config.force == true) {
	sequelize.query('DROP DATABASE IF EXISTS ' + config.development.database + ';' +
					'CREATE DATABASE IF NOT EXISTS ' + config.development.database + ';' ).then(data => {
        //Database dropped
    });
}
else {
	sequelize.query('CREATE DATABASE IF NOT EXISTS ' + config.development.database + ';' ).then(data => {
		//Database created
	});
}
