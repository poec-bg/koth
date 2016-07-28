package services;

import models.Checkin;
import models.Player;
import models.Position;
import models.Zone;
import org.joda.time.DateTime;
import services.date.DateService;
import services.position.FixedPositionService;
import services.position.PositionService;

public class CheckinService {

    public static Checkin checkin(Player player) {
        Checkin checkin = new Checkin();

        PositionService.get().configureWith(new FixedPositionService() {
            @Override
            public Position currentPosition(Player player) {
                return new Position(49.474298f, 1.110211f);
            }
        });
        Position position = PositionService.get().currentPosition(player);

        Zone zone = ZoneService.getZone(position);

        checkin.date = DateService.get().currentDateTime().toDate();
        checkin.latitude = position.latitude;
        checkin.longitude = position.longitude;
        checkin.player = player;
        checkin.zone = zone;
        checkin.save();
        return checkin;
    }

}
