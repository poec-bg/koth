package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class ZoneState extends Model {

    @ManyToOne
    public Zone zone;

    @ManyToOne
    public Player player;

    public Date since;

    public Long harvestedQuantity;

    public ZoneState() {
        harvestedQuantity = 0L;
    }
}
