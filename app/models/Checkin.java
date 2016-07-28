package models;

import org.joda.time.DateTime;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Checkin extends Model{
    public float latitude;
    public float longitude;
    @ManyToOne
    public Player player;
    @ManyToOne
    public Zone zone;
    public Date date;
}
