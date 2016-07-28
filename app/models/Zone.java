package models;

import org.joda.time.DateTime;
import models.types.Ressource;

public class Zone {
    public float topLatitude;
    public float topLongitude;
    public float bottomLatitude;
    public float bottomLongitude;
    public Ressource ressource;
    public DateTime updateDate;

    public Zone(float topLatitude, float topLongitude, float bottomLatitude, float bottomLongitude) {
        this.topLatitude = topLatitude;
        this.topLongitude = topLongitude;
        this.bottomLatitude = bottomLatitude;
        this.bottomLongitude = bottomLongitude;
    }
}

