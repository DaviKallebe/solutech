import * as multer from 'multer';
import {config} from "../../config";
import { UserController } from "../controllers/User";

export class UserRoutes {
    public userControler: UserController = new UserController();

    //multer configuration for file upload
    public fileStorage = multer.diskStorage({
        destination: function(req, file, callBack) {
            return callBack(null, config.image_upload_folder);
        },
        filename: function(req, file, callBack) {
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
        //user profile get from id_user
        app.get('/user/profile/get/:id_user', this.userControler.getUserProfile);
        //user profile update with id_user
        app.put('/user/profile/update', 
                this.fileUploadHandler.single('imagem'),
                this.userControler.updateUserProfile);
    }
}