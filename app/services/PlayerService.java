package services;

import com.google.common.base.Strings;
import exceptions.InvalidArgumentException;
import exceptions.MetierException;
import models.Player;
import org.mindrot.jbcrypt.BCrypt;
import services.db.DBService;
import validators.EmailValidator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerService {

    private static int BCRYPT_WORKLOAD = 12;

    private static PlayerService instance;

    private PlayerService() {
    }

    public static PlayerService get() {
        if (instance == null) {
            instance = new PlayerService();
        }
        return instance;
    }

    public Player create(String email, String usrPassword, String firstName, String lastName) throws exceptions.InvalidArgumentException, MetierException, SQLException {
        List<String> validationMessages = new ArrayList<>();
        if (Strings.isNullOrEmpty(email)) {
            validationMessages.add("L'email ne peut être null ou vide");
        } else {
            if (!EmailValidator.validate(email)) {
                validationMessages.add("Le format de l'email est invalide");
            }
        }
        if (Strings.isNullOrEmpty(usrPassword)) {
            validationMessages.add("Le mot de passe ne peut être null ou vide");
        }
        if (validationMessages.size() > 0) {
            throw new exceptions.InvalidArgumentException((String[]) validationMessages.toArray(new String[0]));
        }

        try {
            Statement requete = DBService.get().getConnection().createStatement();
            ResultSet result = requete.executeQuery("SELECT * FROM Player WHERE email='" + email + "'");
            if (result.next()) {
                throw new MetierException("Cet email est déjà utilisé");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Player player = new Player();
        player.email = email;
        player.usrPassword = encodePassword(usrPassword);
        player.firstName = firstName;
        player.lastName = lastName;

        player.save();

        PreparedStatement preparedStatement = DBService.get().getConnection().prepareStatement("INSERT INTO Player (`email`, `usrPassword`, `firstName`, `lastName`) VALUES (?, ? , ? , ? )");
        preparedStatement.setString(1, player.email);
        preparedStatement.setString(2, player.usrPassword);
        preparedStatement.setString(3, player.firstName);
        preparedStatement.setString(4, player.lastName);

        return player;
    }

    public void supprimer(Player player) throws InvalidArgumentException {
        if (player == null) {
            throw new InvalidArgumentException(new String[]{"Le client ne peut être null"});
        }
        player.isSupprime = true;

        try {
            PreparedStatement preparedStatement = DBService.get().getConnection().prepareStatement("UPDATE Player SET (`isSupprime`) VALUES (?)");
            preparedStatement.setBoolean(1, player.isSupprime);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String encodePassword(String password) {
        String salt = BCrypt.gensalt(BCRYPT_WORKLOAD);
        return BCrypt.hashpw(password, salt);
    }

    public boolean authenticate(String email, String usrPassword) throws InvalidArgumentException {

        List<String> validationMessages = new ArrayList<>();
        if (Strings.isNullOrEmpty(email)) {
            validationMessages.add("Le email ne peut être null ou vide");
        }
        if (Strings.isNullOrEmpty(usrPassword)) {
            validationMessages.add("Le motDePasse ne peut être null ou vide");
        }
        if (validationMessages.size() > 0) {
            throw new InvalidArgumentException((String[]) validationMessages.toArray(new String[0]));
        }

        try {
            Statement requete = DBService.get().getConnection().createStatement();
            ResultSet result = requete.executeQuery("SELECT usrPassword FROM Player WHERE email='" + email + "'");
            if (result.next()) {
                if(BCrypt.checkpw(usrPassword, result.getString("usrPassword"))) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Player getRandom() {
        List<Player> players = Player.findAll();
        return players.get(new Random().nextInt(players.size()));
    }

    public Player getPlayerByEmail(String email) throws exceptions.InvalidArgumentException {

        List<String> validationMessages = new ArrayList<>();
        if (Strings.isNullOrEmpty(email)) {
            validationMessages.add("L'email ne peut être null ou vide");
        }
        if (validationMessages.size() > 0) {
            throw new exceptions.InvalidArgumentException((String[]) validationMessages.toArray(new String[0]));
        }

        try {
            Statement requete = DBService.get().getConnection().createStatement();
            ResultSet result = requete.executeQuery("SELECT * FROM Player WHERE email='" + email + "'");
            if (result.next()) {
                Player player = new Player();
                player.email = result.getString("email");
                player.firstName = result.getString("firstName");
                player.lastName = result.getString("lastName");
                player.isSupprime = result.getBoolean("isSupprime");
                return player;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void clear() {
        try {
            Statement requete = DBService.get().getConnection().createStatement();
            requete.executeUpdate("DELETE FROM Player WHERE isSupprime='" + true + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
