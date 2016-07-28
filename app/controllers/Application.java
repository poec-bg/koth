package controllers;

import models.Checkin;
import models.Player;
import models.Position;
import play.mvc.Controller;
import services.CheckinService;
import services.PlayerService;
import services.RandomPositionService;
import services.position.FixedPositionService;
import services.position.PositionService;

public class Application extends Controller {

    public static void index() {
        PositionService.get().configureWith(new RandomPositionService());
        Checkin checkin = CheckinService.checkin(PlayerService.getRandom());
        render(checkin);
    }

    public static void m2i() {
        PositionService.get().configureWith(new FixedPositionService() {
            @Override
            public Position currentPosition(Player player) {
                return new Position(49.474298f, 1.110211f);
            }
        });
        Checkin checkin = CheckinService.checkin(PlayerService.getRandom());
        renderTemplate("Application/index.html", checkin);
    }

}