import { Request, Response } from 'express';
import { UserController } from "../controllers/User";
import { CommentController } from "../controllers/Comment";
import { UserPlaceController } from "../controllers/UserPlace";
import { MessageController } from "../controllers/Message";

export class Routes {
    public userControler: UserController = new UserController();
    public userPlaceController: UserPlaceController = new UserPlaceController();
    public commentController: CommentController = new CommentController();
    public messageController: MessageController = new MessageController();

    public routes(app): void {
        app.route('/')
            .get((req: Request, res: Response) => {
                res.status(200).send({
                    message: 'GET request successfulll!!!!'
                })
            })

        //firebase authentication
        app.route('/firebaselogin')
            .get(this.userControler.loginWithFirebase);

        app.route('/createfirebaseuser')
            .post(this.userControler.createUserWithFirebase);

        //user related routes
        app.route('/place/:id_user')
            .get(this.userPlaceController.getPlace);

        app.route('/newplace')
            .post(this.userPlaceController.setPlace);

        app.route('/updateplace/:id_user')
            .put(this.userPlaceController.updatePlace);
        
            /*
        app.route('/existplace/:id_user')
            .get(this.userPlaceController.existPlace);*/

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
