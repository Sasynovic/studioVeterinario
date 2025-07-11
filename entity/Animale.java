package entity;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import database.AnimaleDAO;

public class Animale {
    private int chip;
    private String nome;
    private String tipo;
    private String razza;
    private String colore;
    private Date dataNascita;
    private String usernameUtente; // reference to the owner (Utente)

    public Animale() {
    }

    public Animale(int chip, String nome, String tipo,String razza, String colore, Date dataNascita, String usernameUtente) {
        this.chip = chip;
        this.nome = nome;
        this.tipo = tipo;
        this.razza = razza;
        this.colore = colore;
        this.dataNascita = dataNascita;
        this.usernameUtente = usernameUtente;
    }

    public int getChip() {
        return chip;
    }
    public String getNome() {
        return nome;
    }
    public String getTipo() {return tipo;}
    public String getRazza() {
        return razza;
    }
    public String getColore() {
        return colore;
    }
    public Date getDataNascita() {
        return dataNascita;
    }
    public String getUsernameUtente() {
        return usernameUtente;
    }

    public void setChip(int chip) {
        this.chip = chip;
    }

    @Override
    public String toString() {
        return "Nome : " + nome + " - Chip : " + chip;
    }

    public List<Animale> getAllAnimali(String usernameUtente) {
        AnimaleDAO animaleDAO = new AnimaleDAO();
        try {
            return animaleDAO.read(usernameUtente);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle exception appropriately
        }
    }

    public void save(Animale animale) throws SQLException, ClassNotFoundException{
        AnimaleDAO animaleDAO = new AnimaleDAO();
        try {
            animaleDAO.create(animale);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rilancia l'eccezione per gestirla a livello superiore
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw e; // Rilancia l'eccezione per gestirla a livello superiore
        }
    }

    public void delete(Animale animale) {
        AnimaleDAO animaleDAO = new AnimaleDAO();
        try {
            animaleDAO.delete(animale.getChip());
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception appropriately
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Animale animale, int oldChip) {
        AnimaleDAO animaleDAO = new AnimaleDAO();
        try {
            animaleDAO.update(animale, oldChip);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception appropriately
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
