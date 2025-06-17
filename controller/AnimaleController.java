package controller;

import entity.Animale;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class AnimaleController {

    public void addAnimale(int chip, String nome, String tipo, String razza, String colore, Date dataNascita, String usernameUtente) throws SQLException, ClassNotFoundException{
        Animale animale = new Animale(chip, nome, tipo, razza, colore, dataNascita, usernameUtente);
        animale.save(animale);
    }

    public List<Animale> readAnimale(String usernameProprietario){
        Animale animale = new Animale();
        return animale.getAllAnimali(usernameProprietario);
    }

    public void deleteAnimale(int chip){
        Animale animale = new Animale();
        animale.setChip(chip);

        animale.delete(animale);
    }

    public void updateAnimale(int chip, String nome, String tipo, String razza, String colore, Date dataNascita, int oldChip){
        Animale animale = new Animale(chip, nome, tipo, razza, colore, dataNascita, null);
        animale.update(animale, oldChip);
    }
}