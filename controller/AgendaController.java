package controller;

import java.util.Date;
import java.util.List;

import entity.Agenda;

public class AgendaController {

    public List<Agenda> getPrenotazioniDisponibili(Date data) {
        Agenda agenda = new Agenda();
        return agenda.getDisponibili(data);
    }

    public List<Agenda> getOrariDisponibili(Date data) {
        Agenda agenda = new Agenda();
        return agenda.getOrariDisponibili(data);
    }

    public List<Agenda> getPrenotazioniDay(Date data) {
        Agenda agenda = new Agenda();
        return agenda.getPrenotazioneByDate(data);
    }

    public List<Agenda> getVisiteDay(Date data) {
        Agenda agenda = new Agenda();
        return agenda.getVisiteByDate(data);
    }

    public List<Agenda> getVaccinazioniYear(Date data) {
        // spostiamo la data ad un anno fa
        long oneYearAgo = data.getTime() - (365L * 24 * 60 * 60 * 1000); // 365 giorni in millisecondi
        Date oneYearAgoDate = new Date(oneYearAgo);
        Agenda agenda = new Agenda();
        return agenda.getVaccinazioniYear(oneYearAgoDate);
    }


}
