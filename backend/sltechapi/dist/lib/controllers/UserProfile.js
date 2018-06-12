"use strict";
const UserProfile_1 = require("../models/UserProfile");
class UserProfileController {
    updateProfile(req, res) {
        UserProfile_1.UserProfile.update(req.body, {
            where: {
                id_user: req.params.id_user
            }
        });
    }
}
exports.UserProfileController = UserProfileController;
//# sourceMappingURL=UserProfile.js.map