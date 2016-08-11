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
import java.util.UUID;

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

        return player;
    }

    public void supprimer(Player player) throws InvalidArgumentException {
        if (player == null) {
            throw new InvalidArgumentException(new String[]{"Le client ne peut être null"});
        }
        player.isSupprime = true;
        player.merge();

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
                player.id = Long.valueOf(result.getString("id"));
                player.email = result.getString("email");
                player.firstName = result.getString("firstName");
                player.lastName = result.getString("lastName");
                player.isSupprime = result.getBoolean("isSupprime");;
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
