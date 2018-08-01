import * as multer from 'multer';
import { UserPlaceController } from "../controllers/UserPlace";

export class UserPlaceRoutes {
    public userPlaceController: UserPlaceController = new UserPlaceController();

    //multer configuration for file upload
    public fileStorage = multer.diskStorage({
        destination: function(req, file, callBack) {
            return callBack(null, "lugar");
        },
        filename: function(req, file, callBack) {
            console.log(file);
            return callBack(null, new Date().toISOString().replace(/:/g, '-') + file.originalname);
        }
    });

    //mimetypes for image --jpeg and png only
    public fileFilter = (req, file, cb) => {
        if (file.mimetype === 'image/jpeg' || file.mimetype === 'image/png') {
            cb(null, true);
        } else {
            cb(null, false);
        }
    };

    //multer itself with params from early
    public fileUploadHandler = multer({
        fileFilter: this.fileFilter,
        storage: this.fileStorage,
        limits: {
            fileSize: 1024 * 1024 * 1 //limit up to 1MB
        }
    });

    public routes(app): void {
        app.get("/logradouro/selecionar/:id_user",
            this.userPlaceController.selecionarLogradouro);
        app.post("/logradouro/criar",
            this.fileUploadHandler.single('imagem'),
            this.userPlaceController.criarLogradouro);
        app.put("/logradouro/atualizar",
            this.fileUploadHandler.single('imagem'),
            this.userPlaceController.atualizarLogradouro);
    }
}