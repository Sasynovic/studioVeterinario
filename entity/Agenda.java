package entity;

import database.AgendaDAO;
import database.PrenotazioneDAO;

import java.util.Date;
import java.util.List;

public class Agenda {

    public Date data;
    public int orario;

    public String nomeStato;

    public int chipAnimale;
    public String nomeAnimale;

    public String nomeProprietario;

    public Agenda() {}

    public Agenda(Date data, int orario, String nomeStato, int chipAnimale, String nomeAnimale, String nomeProprietario) {
        this.data = data;
        this.orario = orario;
        this.nomeStato = nomeStato;
        this.chipAnimale = chipAnimale;
        this.nomeAnimale = nomeAnimale;
        this.nomeProprietario = nomeProprietario;
    }

    public Date getData() {return data;}
    public int getOrario() {
        return orario;
    }
    public String getNomeStato() {
        return nomeStato;
    }
    public int getChipAnimale() {
        return chipAnimale;
    }
    public String getNomeAnimale() {
        return nomeAnimale;
    }
    public String getNomeProprietario() {
        return nomeProprietario;
    }

    public List<Agenda> getDisponibili(Date data) {
        AgendaDAO agendaDAO = new AgendaDAO();
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
            return agendaDAO.readDisponibili(sqlDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<Agenda> getPrenotazioneByDate(Date data) {
        AgendaDAO agendaDAO = new AgendaDAO();
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
            return agendaDAO.readPrenotazioniToday(sqlDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Agenda> getVisiteByDate(Date data) {
        AgendaDAO agendaDAO = new AgendaDAO();
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
            return agendaDAO.readVisiteToday(sqlDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Agenda> getVaccinazioniYear(Date data) {
        AgendaDAO agendaDAO = new AgendaDAO();
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
            return agendaDAO.readVaccinazioniYear(sqlDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
