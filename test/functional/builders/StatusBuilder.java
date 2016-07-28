package functional.builders;

import functional.CoaxysFunctionalTest;
import play.mvc.Http.Response;

/**
 * <p>Builder permettant de tester les status</p>
 *
 * @author nicogiard
 */
public class StatusBuilder {

    private static StatusBuilder instance;

    public static Response response;

    private StatusBuilder(Response response) {
        this.response = response;
    }

    public static StatusBuilder create(Response response) {
        instance = new StatusBuilder(response);
        return instance;
    }

    /**
     * <p>L'action a t'elle retournée un status 200 (OK) ?</p>
     * <p>Pour mémoire : <a href="http://fr.wikipedia.org/wiki/Liste_des_codes_HTTP">Les Codes HTTP</a></p>
     * @return une instance d'<tt>ContentBuilder</tt> initialisée avec la <tt>response</tt> de l'action
     */
    public ContentBuilder shouldBeOK() {
        return shouldHaveAStatusOf(200);
    }

    /**
     * <p>L'action a t'elle retournée un status 302 (Found) ?</p>
     * <p>Pour mémoire : <a href="http://fr.wikipedia.org/wiki/Liste_des_codes_HTTP">Les Codes HTTP</a></p>
     * @return une instance d'<tt>ContentBuilder</tt> initialisée avec la <tt>response</tt> de l'action
     */
    public ContentBuilder shouldBeFound() {
        return shouldHaveAStatusOf(302);
    }

    /**
     * <p>L'action a t'elle retournée un status 403 (Forbidden) ?</p>
     * <p>Pour mémoire : <a href="http://fr.wikipedia.org/wiki/Liste_des_codes_HTTP">Les Codes HTTP</a></p>
     * @return une instance d'<tt>ContentBuilder</tt> initialisée avec la <tt>response</tt> de l'action
     */
    public ContentBuilder shouldBeForbidden() {
        return shouldHaveAStatusOf(403);
    }

    /**
     * <p>L'action a t'elle retournée un status 404 (Not Found) ?</p>
     * <p>Pour mémoire : <a href="http://fr.wikipedia.org/wiki/Liste_des_codes_HTTP">Les Codes HTTP</a></p>
     * @return une instance d'<tt>ContentBuilder</tt> initialisée avec la <tt>response</tt> de l'action
     */
    public ContentBuilder shouldBeNotFound() {
        return shouldHaveAStatusOf(404);
    }

    /**
     * <p>L'action a t'elle retournée un status particulier ?</p>
     * <p>Pour mémoire : <a href="http://fr.wikipedia.org/wiki/Liste_des_codes_HTTP">Les Codes HTTP</a></p>
     * @return une instance d'<tt>ContentBuilder</tt> initialisée avec la <tt>response</tt> de l'action
     */
    public ContentBuilder shouldHaveAStatusOf(int status) {
        CoaxysFunctionalTest.assertStatus(status, this.response);
        return ContentBuilder.create(this.response);
    }
}