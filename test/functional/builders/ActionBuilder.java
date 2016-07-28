package functional.builders;

import com.google.common.collect.Maps;
import functional.CoaxysFunctionalTest;
import play.mvc.Http;
import play.mvc.Http.Response;
import play.mvc.Router;
import play.mvc.Router.ActionDefinition;

import java.util.Map;

/**
 * <p>Builder permettant de lancer des actions (GET ou POST)</p>
 * <p/>
 * <p>ex : <tt>goTo("Accueil.index")</tt> etc...</p>
 * <p>ex : <tt>withParam("id", 12).goTo("Page.detail")</tt> etc...</p>
 * <p/>
 * <p>Généralement, l'instance à cette classe est crée depuis un <tt>AfterLoginBuilder</tt></p>
 *
 * @author nicogiard
 */
public class ActionBuilder {

    private static ActionBuilder instance;

    public static Map<String, Object> params;

    public static Map<String, Object> bodyParams;

    public static Map<String, Http.Header> headers;

    public static Response response;

    private ActionBuilder(Response response) {
        this.response = response;
        params = null;
        bodyParams = null;
    }

    public static ActionBuilder create() {
        instance = new ActionBuilder(null);
        return instance;
    }

    public static ActionBuilder create(Response response) {
        instance = new ActionBuilder(response);
        return instance;
    }

    /**
     * Ajoute un paramètre, pour l'utilisation ultérieure dans une action (<tt>goTo</tt> ou bien <tt>post</tt> par exemple)
     *
     * @param key   la clé (à mettre en rapport avec les paramètres de votre méthode de controller)
     * @param value la valeur du paramètre
     * @return une instance d'<tt>ActionBuilder</tt> augmentée avec le paramètre
     */
    public ActionBuilder withParam(String key, Object value) {
        if (params == null) {
            params = Maps.newHashMap();
        }
        params.put(key, value);
        return instance;
    }

    public ActionBuilder withBodyParam(String key, Object value) {
        if (bodyParams == null) {
            bodyParams = Maps.newHashMap();
        }
        bodyParams.put(key, value);
        return instance;
    }

    /**
     * Ajoute un header, pour l'utilisation ultérieure dans une action (<tt>goTo</tt> ou bien <tt>post</tt> par exemple)
     *
     * @param key   la clé (à mettre en rapport avec les headers de votre méthode de controller)
     * @param value la valeur du header
     * @return une instance d'<tt>ActionBuilder</tt> augmentée avec le header
     */
    public ActionBuilder withHeader(String key, String value) {
        if (headers == null) {
            headers = Maps.newHashMap();
        }
        headers.put(key, new Http.Header(key, value));
        return instance;
    }

    /**
     * Ajoute un header, pour l'utilisation ultérieure dans une action (<tt>goTo</tt> ou bien <tt>post</tt> par exemple)
     *
     * @param key   la clé (à mettre en rapport avec les headers de votre méthode de controller)
     * @param actionDefinition l'actionDefinition à mettre dans la valeur du header
     * @return une instance d'<tt>ActionBuilder</tt> augmentée avec le header
     */
    public ActionBuilder withHeader(String key, ActionDefinition actionDefinition) {
        if (headers == null) {
            headers = Maps.newHashMap();
        }
        headers.put(key, new Http.Header(key, actionDefinition.toString()));
        return instance;
    }

    /**
     * Execute une action particulière en mode GET (avec ou sans paramètre)
     *
     * @param action l'action sous la forme <tt>"Controller.method"</tt> (à mettre en rapport avec votre méthode de controller)
     * @return une instance d'<tt>StatusBuilder</tt> initialisée avec la <tt>response</tt> de l'action
     */
    public StatusBuilder goTo(String action) {
        return goTo(action, false);
    }

    /**
     * Execute une action particulière en mode GET (avec ou sans paramètre)
     *
     * @param action         l'action sous la forme <tt>"Controller.method"</tt> (à mettre en rapport avec votre méthode de controller)
     * @param followRedirect l'action doit elle suivre si un redirect est reÃ§u
     * @return une instance d'<tt>StatusBuilder</tt> initialisée avec la <tt>response</tt> de l'action
     */
    public StatusBuilder goTo(String action, boolean followRedirect) {
        ActionDefinition actionDefinition = null;
        if (params == null) {
            actionDefinition = Router.reverse(action);
        } else {
            actionDefinition = Router.reverse(action, params);
        }
        if (headers == null) {
            response = CoaxysFunctionalTest.GET(actionDefinition, followRedirect);
        } else {
            response = CoaxysFunctionalTest.GET(actionDefinition, headers, followRedirect);
        }
        return StatusBuilder.create(response);
    }

    public StatusBuilder goToAjax(String action) {
        Http.Request request = CoaxysFunctionalTest.newRequest();
        request.headers.put("x-requested-with", new Http.Header("x-requested-with", "XMLHttpRequest"));
        if (params == null) {
            response = CoaxysFunctionalTest.GET(request, Router.reverse(action));
        } else {
            response = CoaxysFunctionalTest.GET(request, Router.reverse(action, params));
        }
        return StatusBuilder.create(response);
    }

    /**
     * Execute une action particulière en mode POST (avec ou sans paramètre)
     *
     * @param action l'action sous la forme <tt>"Controller.method"</tt> (à mettre en rapport avec votre méthode de controller)
     * @return une instance d'<tt>StatusBuilder</tt> initialisée avec la <tt>response</tt> de l'action
     */
    public StatusBuilder post(String action) {
        return post(action, false);
    }

    /**
     * Execute une action particulière en mode POST (avec ou sans paramètre)
     *
     * @param action         l'action sous la forme <tt>"Controller.method"</tt> (à mettre en rapport avec votre méthode de controller)
     * @param followRedirect l'action doit elle suivre si un redirect est reÃ§u
     * @return une instance d'<tt>StatusBuilder</tt> initialisée avec la <tt>response</tt> de l'action
     */
    public StatusBuilder post(String action, boolean followRedirect) {
        ActionDefinition actionDefinition = null;
        if (params == null) {
            actionDefinition = Router.reverse(action);
        } else {
            actionDefinition = Router.reverse(action, params);
        }
        String body = getRequestBody(bodyParams);
        response = CoaxysFunctionalTest.POST(actionDefinition, headers, followRedirect, body);

        return StatusBuilder.create(response);
    }

    private String getRequestBody(Map<String, Object> bodyParams) {
        String bodyString = "";
        if (bodyParams != null && bodyParams.size() > 0) {
            StringBuilder bodyBuilder = new StringBuilder();
            for (Map.Entry<String, Object> entry : bodyParams.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                bodyBuilder.append(key);
                bodyBuilder.append("=");
                bodyBuilder.append(value);
                bodyBuilder.append("&");
            }
            bodyString = bodyBuilder.toString();
            if (bodyString.endsWith("&")) {
                bodyString = bodyString.substring(0, bodyString.length() - 1);
            }
        }
        return bodyString;
    }
}