package entity;

import database.FarmacoDAO;
import database.VisitaDAO;

public class Visita {

    public int id;
    public String tipoVisita;
    public String descrizione;
    public double costo;
    public String usernameVet;

    public Visita(String tipoVisita, String descrizione, double costo, String usernameVet) {
        this.tipoVisita = tipoVisita;
        this.descrizione = descrizione;
        this.costo = costo;
        this.usernameVet = usernameVet;
    }

    public String getTipoVisita() {
        return tipoVisita;
    }
    public String getDescrizione() {
        return descrizione;
    }
    public double getCosto() {
        return costo;
    }
    public String getUsernameVet() {
        return usernameVet;
    }

    public int saveVisita(Visita v) {
        VisitaDAO visitaDAO = new VisitaDAO();
        try{
            return visitaDAO.create(v);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return -1; // Placeholder, da implementare correttamente
    }


}
