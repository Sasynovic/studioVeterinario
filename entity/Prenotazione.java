package entity;

import java.util.Date;

import database.PrenotazioneDAO;

public class Prenotazione {

    private Date data;
    private int orario;
    private int idStato;
    private int chipAnimale;
    private int idVisita;

    public Prenotazione(){};

    public Date getData() {
        return data;
    }
    public void setData(Date data) {
        this.data = data;
    }

    public int getOrario() {
        return orario;
    }
    public void setOrario(int orario) {
        this.orario = orario;
    }

    public int getIdStato() {
        return idStato;
    }
    public void setIdStato(int idStato) {
        this.idStato = idStato;
    }

    public int getChipAnimale() {
        return chipAnimale;
    }
    public void setChipAnimale(int chipAnimale) {
        this.chipAnimale = chipAnimale;
    }

    public int getIdVisita() {
        return idVisita;
    }

    public void inserisciPrenotazioneAdmin(Prenotazione p){
        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
        try{
            prenotazioneDAO.create(p);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void inserisciPrenotazioneUtente(Prenotazione p){
        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
        try{
            prenotazioneDAO.update(p);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePrenotazioneVet(Date data, int ora, int idVisita, int idStato) {
        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
        try {
            prenotazioneDAO.updateVet(data, ora, idVisita, idStato);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
