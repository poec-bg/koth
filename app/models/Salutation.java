package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Salutation extends Model {

    @ManyToOne
    public Player from;

    @ManyToOne
    public Player to;

    public boolean isRead;

    public Salutation(Player from, Player to) {
        this.from = from;
        this.to = to;
        this.isRead = false;
    }
}
