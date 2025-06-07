package models;

import java.util.Date;

public class Slot {

    private int id;
    private Date data;
    private int orario;
    private boolean disponibile;

    public Slot() {}
    public Slot(int id, Date data, int orario) {
        this.id = id;
        this.data = data;
        this.orario = orario;
        this.disponibile = false;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public Date getData() {return data;}
    public void setData(Date data) {this.data = data;}

    public int getOrario() {return orario;}
    public void setOrario(int orario) {this.orario = orario;}

    public boolean isDisponibile() {return disponibile;}
    public void setDisponibile(boolean disponibile) {this.disponibile = disponibile;}

}
