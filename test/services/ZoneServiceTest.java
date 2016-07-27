package services;

import models.types.Ressource;
import org.junit.Test;
import play.test.UnitTest;

import java.util.Arrays;

public class ZoneServiceTest extends UnitTest {

    @Test
    public void testRandomRessource_OK(){
        // Given

        // When
        Ressource ressource = ZoneService.randomRessource();
        // Then
        assertTrue(Arrays.asList(Ressource.values()).contains(ressource));
    }

//    @Test
//    public void testCreerZone_OK(){
//        // Given
//        float latitude = 1.1f;
//        float longitude = 1.2f;
//        // When
//        Zone zone = ZoneService.creerZone(latitude,longitude);
//        // Then
//        assertNotNull(zone);
//    }
}
