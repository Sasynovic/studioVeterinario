package controller;

import entity.Visita;

public class VisitaController {

    public int inserisciVisita(String tipoVisita, String descrizione, double costo, String usernameVet) {
        Visita farmaco = new Visita(tipoVisita, descrizione, costo, usernameVet);
        return farmaco.saveVisita(farmaco);
    }

}