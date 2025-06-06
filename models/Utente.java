package models;

public abstract class Utente {

    protected String nome;
    protected String cognome;

    protected String username;
    protected String password;

    protected String email;

    protected int tipoutente;

    public Utente(String nome,  String cognome, String username, String password, String email,int tipoutente) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.email = email;
        this.tipoutente = tipoutente; // Default type, can be overridden
    }

    protected void setTipoUtente(int tipoutente) {
        this.tipoutente = tipoutente;
    }

    public abstract void registrazione();

    public final boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

}


