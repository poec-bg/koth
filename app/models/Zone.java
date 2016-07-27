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
}
