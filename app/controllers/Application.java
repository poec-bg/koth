package controllers;

import controllers.secure.Secure;
import controllers.secure.Security;
import exceptions.InvalidArgumentException;
import exceptions.MetierException;
import models.*;
import models.types.ActionPossible;
import play.data.validation.Email;
import play.data.validation.Required;
import play.mvc.Controller;
import services.*;
import services.position.FixedPositionService;
import services.position.PositionService;
import services.position.RandomPositionService;

import java.util.List;

public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void game() {
        PositionService.get().configureWith(new RandomPositionService());
        Checkin checkin = CheckinService.checkin(Security.connectedUser());
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

        Checkin checkin = CheckinService.checkin(Security.connectedUser());

        List<ActionPossible> actionsPossibles = ActionService.listerAction(checkin);

        ZoneState zoneState = ZoneService.getZoneState(checkin.zone);

        List<Salutation> salutations = SalutationService.findUnreadForPlayer(checkin.player);
        SalutationService.markAsRead(salutations);

        renderTemplate("Application/game.html", checkin, zoneState, actionsPossibles, salutations);
    }

    public static void action(String idCheckin, String actionPossible) {
        Checkin checkin = CheckinService.getCheckin(idCheckin);
        ActionService.execute(ActionPossible.valueOf(actionPossible), checkin);
        zone(checkin.latitude, checkin.longitude);
    }

    public static void newPlayer() {
        render();
    }

    public static void createNewPlayer(@Required @Email String email, @Required String usrPassword, @Required String firstName, @Required String lastName) throws Throwable {
        if (validation.hasErrors()) {
            params.flash(); // add http parameters to the flash scope
            validation.keep(); // keep the errors for the next request
            newPlayer();
        }

        try {
            Player player = PlayerService.get().create(email, usrPassword, firstName, lastName );
        } catch (InvalidArgumentException | MetierException e) {
            error(e);
        }
        Application.index();
    }



    public static void deletePlayer() throws Throwable {
        render();
    }

    public static void deleteThisPlayer() throws Throwable {
        Player player = null;
        Checkin checkin = null;

        if (validation.hasErrors()) {
            params.flash(); // add http parameters to the flash scope
            validation.keep(); // keep the errors for the next request
            deletePlayer();
        }

        try {
            player = Security.connectedUser();
            notFoundIfNull(player);
            // doit trouver un moyen de supprimer tous les checkin lié au player pour pouvoir supprimer le player de la bdd
            PlayerService.get().supprimer(player);
            PlayerService.get().clear();


        } catch (InvalidArgumentException e) {
            error(e);
        }
        Application.index();
    }
}
