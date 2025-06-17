package entity;

import database.FarmacoDAO;

import java.util.List;

public class Farmaco {

    private int id;
    private String nome;
    private String produttore;

    public Farmaco() {}

    public Farmaco(int id,String nome, String produttore) {
        this.id = id;
        this.nome = nome;
        this.produttore = produttore;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getProduttore() {
        return produttore;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setProduttore(String produttore) {
        this.produttore = produttore;
    }

    public int saveFarmaco(Farmaco f){
        FarmacoDAO farmacoDao = new FarmacoDAO();
        try{
            return farmacoDao.create(f);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return -1; // Indica un errore nell'inserimento
    }

    public List<Farmaco> getFarmaci() {
        FarmacoDAO farmacoDao = new FarmacoDAO();
        return farmacoDao.read();
    }

    public void impiega(int idFarmaco, int idVisita) {
        FarmacoDAO farmacoDao = new FarmacoDAO();
        try {
            farmacoDao.associaFarmacoVisita(idFarmaco, idVisita);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return  nome + " - > " + produttore;
    }
}
