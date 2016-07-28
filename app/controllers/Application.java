package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

import models.*;
import services.ZoneService;
import services.position.FixedPositionService;
import services.position.PositionService;

public class Application extends Controller {

    public static void index() {

        PositionService.get().configureWith(new FixedPositionService() {
            @Override
            public Position currentPosition(Player player) {
                return new Position(49.474298f, 1.110211f);
            }
        });

        Position position = PositionService.get().currentPosition(null);
        ZoneService.getZone(position);
        render();
    }

}