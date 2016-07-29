package controllers;

import models.*;
import models.types.ActionPossible;
import play.mvc.Controller;
import services.*;
import services.position.FixedPositionService;
import services.position.PositionService;
import services.position.RandomPositionService;

import java.util.List;

public class Application extends Controller {

    public static void index() {
        PositionService.get().configureWith(new RandomPositionService());
        Checkin checkin = CheckinService.checkin(PlayerService.getRandom());
        List<ActionPossible> actionsPossibles = ActionService.listerAction(checkin);
        ZoneState zoneState = ZoneService.getZoneState(checkin.zone);
        List<Salutation> salutations = SalutationService.findUnreadForPlayer(checkin.player);
        SalutationService.markAsRead(salutations);
        render(checkin, zoneState, actionsPossibles, salutations);
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

        ZoneState zoneState = ZoneService.getZoneState(checkin.zone);

        List<Salutation> salutations = SalutationService.findUnreadForPlayer(checkin.player);
        SalutationService.markAsRead(salutations);

        renderTemplate("Application/index.html", checkin, zoneState, actionsPossibles, salutations);
    }

    public static void action(String idCheckin, String actionPossible) {
        Checkin checkin = CheckinService.getCheckin(idCheckin);
        ActionService.execute(ActionPossible.valueOf(actionPossible), checkin);
        zone(checkin.latitude, checkin.longitude);
    }
}
