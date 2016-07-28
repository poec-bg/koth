package models;

import org.joda.time.DateTime;
import models.types.Ressource;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Entity
public class Zone extends Model {

    public float topLatitude;
    public float topLongitude;
    public float bottomLatitude;
    public float bottomLongitude;

    public int x;
    public int y;

    @Enumerated(EnumType.STRING)
    public Ressource ressource;

    public Date updateDate;

}

