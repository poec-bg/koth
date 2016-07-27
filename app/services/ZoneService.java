package services;

import models.types.Ressource;

public class ZoneService {

    public static Ressource randomRessource() {
        double alea = Math.random();
        int nbRessource = Ressource.values().length;
        int index = (int) Math.floor(alea * nbRessource);
        return Ressource.values()[index];
    }

}
