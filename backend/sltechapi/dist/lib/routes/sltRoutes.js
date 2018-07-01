"use strict";
const User_1 = require("../controllers/User");
const Comment_1 = require("../controllers/Comment");
const UserPlace_1 = require("../controllers/UserPlace");
const Message_1 = require("../controllers/Message");
class Routes {
    constructor() {
        this.userControler = new User_1.UserController();
        this.userPlaceController = new UserPlace_1.UserPlaceController();
        this.commentController = new Comment_1.CommentController();
        this.messageController = new Message_1.MessageController();
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
        app.route('/user/list_all')
            .get(this.userControler.listUsers);
        app.route('/user/get_comments/:id_user')
            .get(this.commentController.getComments);
        app.route('/user/get_messages/:id_user')
            .get(this.messageController.getMessages);
        app.route('/user/generate_hashes')
            .get(this.userControler.generateHashes);
    }
}
exports.Routes = Routes;
//# sourceMappingURL=sltRoutes.js.map