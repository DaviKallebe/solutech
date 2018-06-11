"use strict";
const express = require("express");
const bodyParser = require("body-parser");
const sltRoutes_1 = require("./routes/sltRoutes");
class App {
    constructor() {
        this.routePrv = new sltRoutes_1.Routes();
        this.app = express();
        this.config();
        this.routePrv.routes(this.app);
    }
    config() {
        this.app.use(bodyParser.json());
        this.app.use(bodyParser.urlencoded({ extended: false }));
    }
}
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = new App().app;
//# sourceMappingURL=app.js.map