package services;

import controllers.secure.Security;
import models.Checkin;
import models.Player;
import models.Position;
import models.Zone;
import services.date.DateService;
import services.position.PositionService;

public class CheckinService {

    public static Checkin checkin(Player player) {
        Checkin checkin = new Checkin();
        Position position = PositionService.get().currentPosition(player);

        Zone zone = ZoneService.getZone(position);

        checkin.date = DateService.get().currentDateTime().toDate();
        checkin.latitude = position.latitude;
        checkin.longitude = position.longitude;
        checkin.player = Security.connectedUser();
        checkin.zone = zone;
        checkin.save();
        return checkin;
    }

    public static Checkin getCheckin(String idCheckin) {
        return Checkin.findById(Long.parseLong(idCheckin));
    }
}
