package services;

import models.Checkin;
import models.types.ActionPossible;
import org.junit.Test;
import play.test.UnitTest;

import java.util.List;

public class ActionServiceTest extends UnitTest {

    @Test
    public void listerActionTest_planter_drapeau() {
        // Given
        Checkin checkin = null;

        // When
        List<ActionPossible> actions = ActionService.listerAction(checkin);

        // Then
        assertTrue(actions.contains(ActionPossible.PLANTER_DRAPEAU));
    }
}
