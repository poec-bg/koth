package services.position;

import models.Player;
import models.Position;

public abstract class PositionService {

    private static PositionService instance = new GPSPositionService();

    protected PositionService() {
    }

    public static PositionService get() {
        return instance;
    }

    public void configureWith(PositionService positionService) {
        instance = positionService;
    }

    public abstract Position currentPosition(Player player);
}
