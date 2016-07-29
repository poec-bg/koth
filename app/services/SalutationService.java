package services;

import models.Player;
import models.Salutation;

import java.util.List;

public class SalutationService {

    public static List<Salutation> findUnreadForPlayer(Player player) {
        return Salutation.find("to.id = ?1 AND isRead = ?2", player.id, false).fetch();
    }

    public static void markAsRead(List<Salutation> salutations) {
        for (Salutation salutation : salutations) {
            salutation.isRead = true;
            salutation.save();
        }
    }
}
