import { Request, Response } from 'express';
import { UserController } from "../controllers/User";
import { CommentController } from "../controllers/Comment";
import { UserPlaceController } from "../controllers/UserPlace";

export class Routes {
    public userControler: UserController = new UserController();
    public userPlaceController: UserPlaceController = new UserPlaceController();
    public commentController: CommentController = new CommentController();

    public routes(app): void {
        app.route('/')
            .get((req: Request, res: Response) => {
                res.status(200).send({
                    message: 'GET request successfulll!!!!'
                })
            })

        //login normal
        app.route('/login')
            .get(this.userControler.loginNormal);

        app.route('/createnormaluser')
            .post(this.userControler.createUserNormal)

        //login facebook
        app.route('/loginfacebook')
            .get(this.userControler.loginFacebook);

        //place
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
    }
}
