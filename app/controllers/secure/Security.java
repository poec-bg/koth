package controllers.secure;

import exceptions.InvalidArgumentException;
import models.Player;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import services.PlayerService;

public class Security extends Secure.Security {

    public static boolean authenticate(String login, String password) {
        try {
            if (PlayerService.get().authenticate(login, password)) {
                Logger.info("Security | authenticate : connexion de l'utilisateur [%s]", login);
                return true;
            }
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        Logger.warn("Security | authenticate : tentative de connexion de l'utilisateur inconnu [%s]", login);
        return false;
    }

    public static boolean check(String profile) {
        Player connectedPlayer = connectedUser();
        if (profile.equals(connectedPlayer)) {
            return true;
        }
        return false;
    }

    public static String connected() {
        return session.get("username");
    }

    public static boolean isConnected() {
        return session.contains("username");
    }

    public static Player connectedUser() {
        Player user = null;
        try {
            user = PlayerService.get().getPlayerByEmail(connected());
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        if (user == null) {
            redirect("secure.Secure.logout");
        } else {
            renderArgs.put("connectedUser", user);
        }
        return user;
    }

    public static void onAuthenticated() {
        String url = flash.get("url");
        if (StringUtils.isEmpty(url)) {
            redirect("Application.index");
        }
        redirect(url);
    }

    public static void onDisconnect() {
        try {
            Player user = PlayerService.get().getPlayerByEmail(Security.connected());
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        session.clear();
        response.removeCookie("rememberme");
        redirect("Application.index");
    }
}