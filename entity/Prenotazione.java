package entity;

import java.util.Date;
import java.util.List;

import controller.PrenotationResult;
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
    public void setIdVisita(int idVisita) {
        this.idVisita = idVisita;
    }

    public List<Prenotazione> getAllPrenotazioni(Date data, int stato) {
        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
        try {
            // Conversione sicura da java.util.Date a java.sql.Date
            java.sql.Date sqlDate = null;
            if (data != null) {
                // Se la data è già un java.sql.Date, usala direttamente
                if (data instanceof java.sql.Date) {
                    sqlDate = (java.sql.Date) data;
                } else {
                    // Altrimenti converti da java.util.Date
                    sqlDate = new java.sql.Date(data.getTime());
                }
            }
            return prenotazioneDAO.read(sqlDate, stato);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    public List<PrenotationResult> getPrenotazioneByDate(Date data) {
        PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
        try {
            // Conversione sicura da java.util.Date a java.sql.Date
            java.sql.Date sqlDate = null;
            if (data != null) {
                // Se la data è già un java.sql.Date, usala direttamente
                if (data instanceof java.sql.Date) {
                    sqlDate = (java.sql.Date) data;
                } else {
                    // Altrimenti converti da java.util.Date
                    sqlDate = new java.sql.Date(data.getTime());
                }
            }
            return prenotazioneDAO.readPrenotazioniToday(sqlDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
