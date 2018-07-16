import { UserProfile } from "../models/UserProfile";
import { Request, Response } from 'express';

export class UserProfileController {
    public updateProfile(req: Request, res: Response) {
        UserProfile.update(req.body, {
            where: {
                id_user: req.params.id_user
            }
        });
    }
}
