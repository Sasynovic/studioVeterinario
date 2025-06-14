package controller;

import entity.Prenotazione;

import java.util.Date;
import java.util.List;

public class PrenotazioneController {

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
        return prenotazione.getAllPrenotazioni(data, stato);
    }
}
