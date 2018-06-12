"use strict";
const User_1 = require("../controllers/User");
const UserPlace_1 = require("../controllers/UserPlace");
class Routes {
    constructor() {
        this.userControler = new User_1.UserController();
        this.userPlaceController = new UserPlace_1.UserPlaceController();
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
        app.route('/loginfacebook')
            .get(this.userControler.loginFacebook);
        app.route('/place/:id_user')
            .get(this.userPlaceController.getPlace);
        app.route('/newplace')
            .post(this.userPlaceController.setPlace);
        app.route('/updateplace/:id_user')
            .put(this.userPlaceController.updatePlace);
        app.route('/existplace/:id_user')
            .get(this.userPlaceController.existPlace);
    }
}
exports.Routes = Routes;
//# sourceMappingURL=sltRoutes.js.map