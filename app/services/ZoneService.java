package services;

import models.Zone;
import models.types.Ressource;

public class ZoneService {

    public static Ressource randomRessource() {
        double alea = Math.random();
        int nbRessource = Ressource.values().length;
        int index = (int) Math.floor(alea * nbRessource);
        return Ressource.values()[index];
    }

    public static Zone getDefaultZone() {
        Zone zoneInit = new Zone(10, 10, 0, 0);
        return zoneInit;
    }
}
