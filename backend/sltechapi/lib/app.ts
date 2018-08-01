import * as express from 'express';
import * as bodyParser from "body-parser";
import { Routes } from "./routes/sltRoutes";
import { UserRoutes} from "./routes/User";
import { config } from "../config";
import { HospedagemRoutes } from './routes/Hospedagem';
import { UserPlaceRoutes } from './routes/UserPlace';

class App {

    public app: express.Application;
    public routePrv: Routes = new Routes();
    public userRoutes: UserRoutes = new UserRoutes();
    public hospedagemRoutes: HospedagemRoutes = new HospedagemRoutes();
    public userPlaceRoutes: UserPlaceRoutes = new UserPlaceRoutes();

    constructor() {
        this.app = express();
        this.config();
        this.routePrv.routes(this.app);
        this.userRoutes.routes(this.app);
        this.hospedagemRoutes.routes(this.app);
        this.userPlaceRoutes.routes(this.app);
    }

    private config(): void{
        //supporte application/json type post data
        this.app.use(bodyParser.json());
        //support application/x-www-form-urlencoded post data
        this.app.use(bodyParser.urlencoded({ extended: false }));
        //images folder public
        this.app.use('/images', express.static(config.image_upload_folder));
        this.app.use('/lugar', express.static(config.image_upload_folder));
        //suport fireadmin-base
        //nothing for while
    }

}

export default new App().app;
