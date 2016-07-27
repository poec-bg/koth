package services;

import models.Checkin;
import models.Player;
import models.Position;
import models.Zone;
import org.joda.time.DateTime;

public class CheckinService {

    public static Checkin checkin(Player player) {
        Checkin checkin = new Checkin();
        Position position = ouSuisJe(player);
        Zone zone = dansQuelleZone(position.latitude, position.longitude);

        checkin.date = DateTime.now();
        checkin.latitude = position.latitude;
        checkin.longitude = position.longitude;
        checkin.player = player;
        checkin.zone = zone;
        return checkin;
    }

    private static Position ouSuisJe(Player player) {
        Position position = new Position();
        position.latitude=player.position.latitude;
        position.longitude=player.position.longitude;
        return position;


    }

    private static Zone dansQuelleZone(float latitude, float longitude) {
        return null;
    }

}
