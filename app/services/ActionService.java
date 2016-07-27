package services;

import models.Checkin;
import models.types.ActionPossible;

import java.util.Arrays;
import java.util.List;

public class ActionService {

    public static List<ActionPossible> listerAction(Checkin checkin) {
        return Arrays.asList(ActionPossible.PLANTER_DRAPEAU);
    }

}
