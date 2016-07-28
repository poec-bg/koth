package services;

import models.Action;
import models.Checkin;
import models.ZoneState;
import models.types.ActionPossible;
import org.joda.time.DateTime;
import services.date.DateService;

import java.util.Arrays;
import java.util.List;

public class ActionService {

    public static List<ActionPossible> listerAction(Checkin checkin) {

        // Retrouver les actions sur la même zone que le checkin, entre y'a 5 minutes et maintenant, fait par d'autre player que celui de ce checkin

        List<Action> actions = Action.find("zone.id = ?1 AND player.id != ?2 AND date BETWEEN ?3 AND ?4 AND action = ?5", checkin.zone.id, checkin.player.id, new DateTime(checkin.date).minusMinutes(1).toDate(), new DateTime(checkin.date).toDate(), ActionPossible.PLANTER_DRAPEAU).fetch();

        if (actions.size() > 0) {
            return Arrays.asList(ActionPossible.SALUER);
        } else {
            return Arrays.asList(ActionPossible.PLANTER_DRAPEAU);
        }
    }

    public static void execute(ActionPossible actionPossible, Checkin checkin) {
        Action action = new Action();
        action.date = DateService.get().currentDateTime().toDate();
        action.action = actionPossible;
        action.player = checkin.player;
        action.zone = checkin.zone;
        action.save();

        if(action.action.equals(ActionPossible.PLANTER_DRAPEAU)) {
            ZoneService.updateZoneState(action);
        }
    }
}
