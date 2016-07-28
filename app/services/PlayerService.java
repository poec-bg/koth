package services;

import models.Player;

import java.util.List;
import java.util.Random;

public class PlayerService {

    public static Player create(String email, String password, String firstName, String lastName) {
        Player player = new Player();
        player.email = email;
        player.password = password;
        player.firstName = firstName;
        player.lastName = lastName;

        player.save();

        return player;
    }


    public static Player getRandom() {
        List<Player> players = Player.findAll();
        return players.get(new Random().nextInt(players.size()));
    }

}
