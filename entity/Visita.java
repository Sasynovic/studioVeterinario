package entity;

import database.FarmacoDAO;
import database.VisitaDAO;

public class Visita {

    public int id;
    public String tipoVisita;
    public String descrizione;
    public double costo;
    public String usernameVet;

    public Visita() {}
    public Visita(String tipoVisita, String descrizione, double costo, String usernameVet) {
        this.tipoVisita = tipoVisita;
        this.descrizione = descrizione;
        this.costo = costo;
        this.usernameVet = usernameVet;
    }

    public int getId() {
        return id;
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
    public void setTipoVisita(String tipoVisita) {
        this.tipoVisita = tipoVisita;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    public void setCosto(double costo) {
        this.costo = costo;
    }
    public void setUsernameVet(String usernameVet) {
        this.usernameVet = usernameVet;
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
