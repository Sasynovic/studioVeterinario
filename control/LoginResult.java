package control;

public class LoginResult {
    private boolean success;
    private int tipoUtente;
    private String message;

    public LoginResult(boolean success, int tipoUtente, String message) {
        this.success = success;
        this.tipoUtente = tipoUtente;
        this.message = message;
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
}

