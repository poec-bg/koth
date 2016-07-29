package services;

import models.Action;
import models.Position;
import models.Zone;
import models.ZoneState;
import models.types.Ressource;
import services.date.DateService;

import java.util.Optional;

public class ZoneService {

    public static Zone getZone(Position position) {
        int x = getZoneXFromLongitude(position.longitude);
        int y = getZoneYFromLatitude(position.latitude);

        Optional<Zone> zone = getZoneFromCoordinates(x, y);

        if (zone.isPresent()) {
            return zone.get();
        } else {
            return createZone(x, y);
        }
    }

    public static ZoneState getZoneState(Zone zone) {
        return ZoneState.find("zone.id = ?1", zone.id).first();
    }

    public static void updateZoneState(Action action) {
        ZoneState zoneState = ZoneState.find("zone.id = ?1", action.zone.id).first();
        if (zoneState == null) {
            zoneState = new ZoneState();
            zoneState.zone = action.zone;
        }
        zoneState.player = action.player;
        zoneState.since = action.date;
        zoneState.save();
    }

    // ----------------------
    // Private

    private static Zone createZone(int x, int y) {
        Zone zone = new Zone();
        zone.x = x;
        zone.y = y;
        zone.topLatitude = getLatitudeFromY(y);
        zone.topLongitude = getLongitudeFromX(x);
        zone.bottomLatitude = getLatitudeFromY(y + 1);
        zone.bottomLongitude = getLongitudeFromX(x + 1);
        zone.ressource = randomRessource();
        zone.updateDate = DateService.get().currentDateTime().toDate();

        zone.save();

        return zone;
    }

    private static Ressource randomRessource() {
        double alea = Math.random();
        int nbRessource = Ressource.values().length;
        int index = (int) Math.floor(alea * nbRessource);
        return Ressource.values()[index];
    }

    private final static long perimeter = 100000;
    private final static long equator = 121212;

    private static int getZoneXFromLongitude(Float longitude) {
        return new Float((perimeter * (longitude + 180)) / 360).intValue();
    }

    private static float getLongitudeFromX(long x) {
        return (360F * x) / perimeter - 180F;
    }

    private static int getZoneYFromLatitude(Float latitude) {
        return new Double(equator - Math.log(Math.tan((Math.PI / 4) + latitude * Math.PI / 180 / 2)) * perimeter / (Math.PI * 2)).intValue();
    }

    private static float getLatitudeFromY(long y) {
        return (float) (-(Math.atan(Math.exp((y - equator) * 2 * Math.PI / perimeter)) - Math.PI / 4) * 360 / Math.PI);
    }

    private static Optional<Zone> getZoneFromCoordinates(int x, int y) {
        return Optional.ofNullable(Zone.find("x=?1 AND y=?2", x, y).first());
    }
}
