package controller;

import java.util.Date;

public class PrenotationResult {

    public Date data;
    public int orario;

    public String nomeStato;

    public int chipAnimale;
    public String nomeAnimale;

    public String nomeProprietario;

    public PrenotationResult(Date data, int orario, String nomeStato, int chipAnimale, String nomeAnimale, String nomeProprietario) {
        this.data = data;
        this.orario = orario;
        this.nomeStato = nomeStato;
        this.chipAnimale = chipAnimale;
        this.nomeAnimale = nomeAnimale;
        this.nomeProprietario = nomeProprietario;
    }

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
}
