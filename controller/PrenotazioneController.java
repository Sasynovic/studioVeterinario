package controller;

import entity.Prenotazione;
import java.util.Date;

public class PrenotazioneController {

    public void inserisciDisponibilita(Date data, int orario, int idStato) {
        Prenotazione prenotazione = new Prenotazione();
            prenotazione.setData(data);
            prenotazione.setOrario(orario);
            prenotazione.setIdStato(idStato);
        prenotazione.inserisciPrenotazioneAdmin(prenotazione);
    }

    public void updatePrenotazioneUtente(Date data, int orario, int chipAnimale, int idStato){
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setData(data);
        prenotazione.setOrario(orario);
        prenotazione.setChipAnimale(chipAnimale);
        prenotazione.setIdStato(idStato);
        prenotazione.inserisciPrenotazioneUtente(prenotazione);
    }

    public void updatePrenotazioneVet(Date data, int ora, int idVisita, int idStato) {
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.updatePrenotazioneVet(data, ora , idVisita, idStato);
    }
}
