import { Request, Response } from "express";
import { UserController } from "../controllers/User"

export class Routes {
    public userControler: UserController = new UserController();

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
    }
}
