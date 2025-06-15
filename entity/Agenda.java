package entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Agenda {

    //Visita
    private int idVisita;
    private String tipoVisita;
    private String descrizione;
    private double costo;

    private String nomeVeterinario;
    private String cognomeVeterinario;

    //Prenotazione
    private Date dataPrenotazione;
    private int orarioPrenotazione;
    private String nomeStato;
    private int chipAnimale;

    //Proprietario
    private String nomeAnimale;
    private String nomeProprietario;

    public Agenda(String tipoVisita, String descrizione, double costo, String chipAnimale,
                  String nomeVeterinario, String cognomeVeterinario, String nomeAnimale,
                  String nomeProprietario, int idVisita, Date dataPrenotazione, int orarioPrenotazione,
                  String nomeStato) {
        this.tipoVisita = tipoVisita;
        this.descrizione = descrizione;
        this.costo = costo;
        this.chipAnimale = Integer.parseInt(chipAnimale);
        this.nomeVeterinario = nomeVeterinario;
        this.cognomeVeterinario = cognomeVeterinario;
        this.nomeAnimale = nomeAnimale;
        this.nomeProprietario = nomeProprietario;
        this.idVisita = idVisita;
        this.dataPrenotazione = dataPrenotazione;
        this.orarioPrenotazione = orarioPrenotazione;
        this.nomeStato = nomeStato;
    }
//
//    public List<Agenda> visualizzaAgenda() {
////        return Prenotazione.visualizzaAgenda(this.nomeProprietario);
//    }
}