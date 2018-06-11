"use strict";
const User_1 = require("../controllers/User");
class Routes {
    constructor() {
        this.userControler = new User_1.UserController();
    }
    routes(app) {
        app.route('/')
            .get((req, res) => {
            res.status(200).send({
                message: 'GET request successfulll!!!!'
            });
        });
        app.route('/login')
            .get(this.userControler.loginNormal);
        app.route('/createnormaluser')
            .post(this.userControler.createUserNormal);
    }
}
exports.Routes = Routes;
//# sourceMappingURL=sltRoutes.js.map