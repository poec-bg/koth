package services;

import models.Checkin;
import models.Player;
import org.junit.Test;
import play.test.UnitTest;

public class CheckinServiceTest extends UnitTest{

    @Test
    public void testCheckin_OK(){
        //Given
        Player player = null;

        //When
        Checkin checkin = CheckinService.checkin(player);

        //Then
        assertNotNull(checkin);
    }

}
