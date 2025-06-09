package entity;

import java.time.LocalDate;

public class Agenda {
    // Dati della Prenotazione
    private String tipoVisita;
    private String descrizione;
    private double costo;
    private String chipAnimale;

    // Dati del Veterinario
    private String nomeVeterinario;
    private String cognomeVeterinario;

    // Dati dell'Animale
    private String nomeAnimale;

    // Dati del Proprietario
    private String nomeProprietario;
    private String cognomeProprietario;

    // Dati dello Slot
    private String idSlot;
    private LocalDate data;
    private int orario;
    private boolean disponibile;

    // Costruttore completo
    public Agenda(String tipoVisita, String descrizione, double costo, String chipAnimale,
                         String nomeVeterinario, String cognomeVeterinario, String nomeAnimale,
                         String nomeProprietario, String cognomeProprietario,
                         String idSlot, LocalDate data, int orario, boolean disponibile) {
        this.tipoVisita = tipoVisita;
        this.descrizione = descrizione;
        this.costo = costo;
        this.chipAnimale = chipAnimale;
        this.nomeVeterinario = nomeVeterinario;
        this.cognomeVeterinario = cognomeVeterinario;
        this.nomeAnimale = nomeAnimale;
        this.nomeProprietario = nomeProprietario;
        this.cognomeProprietario = cognomeProprietario;
        this.idSlot = idSlot;
        this.data = data;
        this.orario = orario;
        this.disponibile = disponibile;
    }

    // Getter e Setter
    public String getTipoVisita() { return tipoVisita; }
    public void setTipoVisita(String tipoVisita) { this.tipoVisita = tipoVisita; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }

    public String getChipAnimale() { return chipAnimale; }
    public void setChipAnimale(String chipAnimale) { this.chipAnimale = chipAnimale; }

    public String getNomeVeterinario() { return nomeVeterinario; }
    public void setNomeVeterinario(String nomeVeterinario) { this.nomeVeterinario = nomeVeterinario; }

    public String getCognomeVeterinario() { return cognomeVeterinario; }
    public void setCognomeVeterinario(String cognomeVeterinario) { this.cognomeVeterinario = cognomeVeterinario; }

    public String getNomeAnimale() { return nomeAnimale; }
    public void setNomeAnimale(String nomeAnimale) { this.nomeAnimale = nomeAnimale; }

    public String getNomeProprietario() { return nomeProprietario; }
    public void setNomeProprietario(String nomeProprietario) { this.nomeProprietario = nomeProprietario; }

    public String getCognomeProprietario() { return cognomeProprietario; }
    public void setCognomeProprietario(String cognomeProprietario) { this.cognomeProprietario = cognomeProprietario; }

    public String getIdSlot() { return idSlot; }
    public void setIdSlot(String idSlot) { this.idSlot = idSlot; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public int getOrario() { return orario; }
    public void setOrario(int orario) { this.orario = orario; }

    public boolean isDisponibile() { return disponibile; }
    public void setDisponibile(boolean disponibile) { this.disponibile = disponibile; }

    @Override
    public String toString() {
        return String.format("AgendaUser{tipoVisita='%s', animale='%s', veterinario='%s %s', data=%s, orario=%s, costo=%.2f}",
                tipoVisita, nomeAnimale, nomeVeterinario, cognomeVeterinario, data, orario, costo);
    }
}