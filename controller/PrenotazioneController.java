package controller;

import entity.Prenotazione;

import java.util.Date;
import java.util.List;

public class PrenotazioneController {

    public List<Prenotazione> readPrenotazione(Date data, int stato) {
        Prenotazione prenotazione = new Prenotazione();
        return prenotazione.getAllPrenotazioni(data, stato);
    }
}
