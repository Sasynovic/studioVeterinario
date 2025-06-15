package entity;

import controller.LoginResult;
import database.UtenteDAO;

import java.sql.SQLException;
import java.util.List;


public class Utente {

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


    public LoginResult loginUtente(String username, String password) throws SQLException, ClassNotFoundException {
        UtenteDAO utenteDao = new UtenteDAO();
        LoginResult result = utenteDao.login(username, password);

        if (result.isSuccess()) {
            this.username = result.getUsername();
            this.nome = result.getNome();
            this.cognome = result.getCognome();
            this.tipoUtente = result.getTipoUtente();
        }

        return result;
    }
    public void save(Utente utente) throws SQLException, ClassNotFoundException {
        UtenteDAO utenteDAO = new UtenteDAO();
        try{
            utenteDAO.create(utente);
        }catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rilancia l'eccezione per gestirla a livello superiore
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw e; // Rilancia l'eccezione per gestirla a livello superiore
        }
    }

    public List<Utente> getUtenti(String username) throws SQLException, ClassNotFoundException {
        UtenteDAO utenteDAO = new UtenteDAO();
        return utenteDAO.read(username);
    }
    public void updateUtente(Utente utente, String useranmeOld) throws SQLException, ClassNotFoundException {
        UtenteDAO utenteDAO = new UtenteDAO();
        try {
            utenteDAO.update(utente,useranmeOld);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rilancia l'eccezione per gestirla a livello superiore
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw e; // Rilancia l'eccezione per gestirla a livello superiore
        }
    }


}


