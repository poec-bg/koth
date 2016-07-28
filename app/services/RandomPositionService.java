package services;

import models.Player;
import models.Position;
import services.position.PositionService;

public class RandomPositionService extends PositionService {

    @Override
    public Position currentPosition(Player player) {
        float latitude = (float) (Math.random() * 180 - 90);
        float longitude = (float) (Math.random() * 360 - 180);
        return new Position(latitude, longitude);
    }

}
