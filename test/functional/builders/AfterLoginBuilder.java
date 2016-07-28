package functional.builders;

import functional.CoaxysFunctionalTest;
import play.mvc.Http.Response;
import play.mvc.Router;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>La classe <tt>AfterLoginBuilder</tt> est le point d'entrée dans nos Tests Fonctionnels.
 * Il permet de commencer par s'authentifier sur une application play.</p>
 *
 * <p>ex : <tt>withLogin("monLogin").andWithPassword("monPassword").login()</tt> etc...;</p>
 *
 * @author nicogiard
 */
public class AfterLoginBuilder {

    private static AfterLoginBuilder instance;

    public static String login;

    public static String password;

    public static Response response;

    protected AfterLoginBuilder() {
    }

    public static ActionBuilder withNoLogin() {
        return ActionBuilder.create(null);
    }

    /**
     * Seul point d'entrée dans l'<tt>AfterLoginBuilder</tt>.
     * <tt>withLogin</tt> permet d'initialiser l'identifiant de l'utilisateur
     * @param login l'identifiant de connexion de l'utilisateur
     * @return l'instance d'<tt>AfterLoginBuilder</tt> initialisée avec le login
     */
    public static AfterLoginBuilder withLogin(String login) {
        if (instance == null) {
            instance = new AfterLoginBuilder();
        }
        instance.login = login;
        return instance;
    }

    /**
     * <tt>andWithPassword</tt> permet d'initialiser le mot de passe de l'utilisateur
     * @param password le mot de passe de connexion de l'utilisateur
     * @return l'instance d'<tt>AfterLoginBuilder</tt> augmentée avec le mot de passe
     */
    public AfterLoginBuilder andWithPassword(String password) {
        this.password = password;
        return instance;
    }

    /**
     * <tt>login</tt> lance l'action d'authentification vers le framework
     * @return une instance d'<tt>ActionBuilder</tt> initialisée avec la <tt>response</tt> d'authentification
     */
    public ActionBuilder login() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", this.login);
        params.put("password", this.password);
        this.response = CoaxysFunctionalTest.POST(Router.reverse("Secure.login", params), true);
        CoaxysFunctionalTest.assertIsOk(this.response);
        return ActionBuilder.create(this.response);
    }
}