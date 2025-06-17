package controller;

import entity.Farmaco;

import java.util.List;

public class FarmacoController {

    public int inserisciFarmaco(String nome, String produttore) {
        Farmaco farmaco = new Farmaco();
        farmaco.setNome(nome);
        farmaco.setProduttore(produttore);
        return farmaco.saveFarmaco(farmaco);

    }
    public List<Farmaco> getFarmaci() {
        Farmaco farmaco = new Farmaco();
        return farmaco.getFarmaci();
    }
    public void impiegaFarmaco(int idFarmaco, int idVisita){
        Farmaco farmaco = new Farmaco();
        farmaco.impiega(idFarmaco, idVisita);
    }

}
