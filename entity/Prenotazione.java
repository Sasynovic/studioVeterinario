package entity;

public class Prenotazione {

    private int id;
    private int stato;
    private String tipoVisita;
    private String descrizione;
    private float costo;
    private int chipAnimale;
    private String usernameVeterinario;
    private int idSlot;

    public Prenotazione(){};

    public Prenotazione(int id, String tipoVisita, int chipAnimale, int idSlot) {
        this.id = id;
        this.stato = 1;
        this.tipoVisita = tipoVisita;
        this.chipAnimale = chipAnimale;
        this.idSlot = idSlot;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public int getStato() {return stato;}
    public void setStato(int stato) {this.stato = stato;}

    public String getTipoVisita() {return tipoVisita;}
    public void setTipoVisita(String tipoVisita) {this.tipoVisita = tipoVisita;}

    public String getDescrizione() {return descrizione;}
    public void setDescrizione(String descrizione) {this.descrizione = descrizione;}

    public float getCosto() {return costo;}
    public void setCosto(float costo) {this.costo = costo;}

    public int getChipAnimale() {return chipAnimale;}
    public void setChipAnimale(int chipAnimale) {this.chipAnimale = chipAnimale;}

    public String getUsernameVeterinario() {return usernameVeterinario;}
    public void setUsernameVeterinario(String usernameVeterinario) {this.usernameVeterinario = usernameVeterinario;}

    public int getIdSlot() {return idSlot;}
    public void setIdSlot(int idSlot) {this.idSlot = idSlot;}


}
