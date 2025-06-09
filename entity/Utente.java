package entity;

public abstract class Utente {

    protected String username;
    protected String nome;
    protected String cognome;
    protected String email;
    protected String password;
    protected String immagineProfilo;
    protected int tipoUtente;

    public Utente() {}
    public Utente(String username, String nome, String cognome, String email, String password, String immagineProfilo, int tipoUtente) {
        this.username = username;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.immagineProfilo = immagineProfilo;
        this.tipoUtente = tipoUtente;
    }

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username; }

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome; }

    public String getCognome() {return cognome;}
    public void setCognome(String cognome) {this.cognome = cognome; }

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email; }

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password; }

    public String getImmagineProfilo() {return immagineProfilo;}
    public void setImmagineProfilo(String immagineProfilo) {this.immagineProfilo = immagineProfilo; }

    public int getTipoUtente() {return tipoUtente;}
    public void setTipoUtente(int tipoUtente) {this.tipoUtente = tipoUtente;}

}


