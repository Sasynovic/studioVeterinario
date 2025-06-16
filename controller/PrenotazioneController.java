package controller;

import entity.Prenotazione;

import java.util.Date;
import java.util.List;

public class PrenotazioneController {

    public void inserisciDisponibilita(Date data, int orario, int idStato) {
        Prenotazione prenotazione = new Prenotazione();
            prenotazione.setData(data);
            prenotazione.setOrario(orario);
            prenotazione.setIdStato(idStato);
        prenotazione.inserisciPrenotazioneAdmin(prenotazione);
    }

    public void updatePrenotazione(Date data, int orario, int chipAnimale, int idStato){
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setData(data);
        prenotazione.setOrario(orario);
        prenotazione.setChipAnimale(chipAnimale);
        prenotazione.setIdStato(idStato); // Assuming 1 is the ID for "in attesa"
        prenotazione.inserisciPrenotazioneUtente(prenotazione);
    }

    public List<Prenotazione> readPrenotazione(Date data, int stato) {
        Prenotazione prenotazione = new Prenotazione();
        return prenotazione.ricercaPrenotazioniDisponibili(data, stato);
    }

    public List<PrenotationResult> getPrenotazioniDay(Date data) {
        Prenotazione prenotazione = new Prenotazione();
        return prenotazione.getPrenotazioneByDate(data);
    }
}
