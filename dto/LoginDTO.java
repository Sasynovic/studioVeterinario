package dto;

public class LoginDTO {
    private boolean success;
    private int tipoUtente;
    private String message;
    private String nome;
    private String cognome;
    private String username;
    private String immagineProfilo;

    public LoginDTO(boolean success, int tipoUtente, String message, String nome, String cognome, String username, String immagineProfilo) {
        this.success = success;
        this.tipoUtente = tipoUtente;
        this.message = message;
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.immagineProfilo = immagineProfilo;
    }

    public boolean isSuccess() {
        return success;
    }
    public int getTipoUtente() {
        return tipoUtente;
    }
    public String getMessage() {
        return message;
    }
    public String getNome() {
        return nome;
    }
    public String getCognome() {
        return cognome;
    }
    public String getUsername() {
        return username;
    }
    public String getImmagineProfilo() {
        return immagineProfilo;
    }
}

