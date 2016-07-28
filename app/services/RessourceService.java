package services;

import models.ZoneState;

import java.util.List;

public class RessourceService {

    public static void harvest() {
        List<ZoneState> zonesStates = ZoneState.findAll();
        for (ZoneState zoneState : zonesStates) {

            if (zoneState.player.ressources.containsKey(zoneState.zone.ressource.name())) {
                zoneState.player.ressources.put(zoneState.zone.ressource.name(), zoneState.player.ressources.get(zoneState.zone.ressource.name()) + 1L);
            } else {
                zoneState.player.ressources.put(zoneState.zone.ressource.name(), 1L);
            }
            zoneState.player.save();
            zoneState.harvestedQuantity += 1L;
            zoneState.save();
        }
    }
}
