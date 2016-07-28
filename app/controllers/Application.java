package controllers;

import models.Checkin;
import models.Player;
import models.Position;
import models.ZoneState;
import models.types.ActionPossible;
import play.mvc.Controller;
import services.ActionService;
import services.CheckinService;
import services.PlayerService;
import services.position.FixedPositionService;
import services.position.PositionService;
import services.position.RandomPositionService;

import java.util.List;

public class Application extends Controller {

    public static void index() {
        PositionService.get().configureWith(new RandomPositionService());
        Checkin checkin = CheckinService.checkin(PlayerService.getRandom());
        List<ActionPossible> actionsPossibles = ActionService.listerAction(checkin);
        render(checkin, actionsPossibles);
    }

    public static void zone(float latitude, float longitude) {
        PositionService.get().configureWith(new FixedPositionService() {
            @Override
            public Position currentPosition(Player player) {
                return new Position(latitude, longitude);
            }
        });
        Checkin checkin = CheckinService.checkin(PlayerService.getRandom());
        List<ActionPossible> actionsPossibles = ActionService.listerAction(checkin);
        ZoneState zoneState = ZoneState.find("zone.id=?1", checkin.zone.id).first();
        renderTemplate("Application/index.html", checkin, zoneState, actionsPossibles);
    }

    public static void action(String idCheckin, String actionPossible) {
        Checkin checkin = CheckinService.getCheckin(idCheckin);
        ActionService.execute(ActionPossible.valueOf(actionPossible), checkin);
        zone(checkin.latitude, checkin.longitude);
    }
}
