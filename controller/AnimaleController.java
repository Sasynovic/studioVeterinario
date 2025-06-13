package controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import entity.Animale;

public class AnimaleController {

    public void addAnimale(int chip, String nome, String tipo, String razza, String colore, Date dataNascita, String usernameUtente) throws SQLException, ClassNotFoundException{
        Animale animale = new Animale(chip, nome, tipo, razza, colore, dataNascita, usernameUtente);
        animale.save(animale);
    }
}