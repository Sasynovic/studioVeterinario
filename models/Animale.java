package models;

public class Animale {
    private int chip;
    private String nome;
    private String razza;
    private String colore;
    private String dataNascita;
    private String usernameUtente; // reference to the owner (Utente)

    public Animale(){}

    public Animale(int chip, String nome, String razza, String colore, String dataNascita, String usernameUtente) {
        this.chip = chip;
        this.nome = nome;
        this.razza = razza;
        this.colore = colore;
        this.dataNascita = dataNascita;
        this.usernameUtente = usernameUtente;
    }

    public int getChip(){return chip; }
    public void setChip(int chip){this.chip = chip; }

    public String getNome(){return nome; }
    public void setNome(String nome){this.nome = nome; }

    public String getRazza(){return razza; }
    public void setRazza(String razza){this.razza = razza; }

    public String getColore(){return colore; }
    public void setColore(String colore){this.colore = colore; }

    public String getDataNascita(){return dataNascita; }
    public void setDataNascita(String dataNascita){this.dataNascita = dataNascita; }

    public String getUsernameUtente() {return usernameUtente;}
    public void setUsernameUtente(String usernameUtente) {this.usernameUtente = usernameUtente;}
}
