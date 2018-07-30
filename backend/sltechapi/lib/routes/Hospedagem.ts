import { HospedagemController } from "../controllers/Hospedagem";

export class HospedagemRoutes {
    public hospedagemController: HospedagemController = new HospedagemController();

    public routes(app): void {
        app.get("/hospedagem/pedinte/:id_user_pedinte",
            this.hospedagemController.selecionarHospedagemPedinte);

        app.get("/hospedagem/hospedador/:id_user_hospedador",
            this.hospedagemController.selecionarHospedagemHospedador);

        app.post("/hospedagem/criar",
            this.hospedagemController.novaHospedagem);
        
        app.put("/hospedagem/atualizar",
            this.hospedagemController.atualizarHospedagem);

        app.get("/hospedagem/pets/:id_pets",
            this.hospedagemController.selecionarPetHospedagem);
    }
}