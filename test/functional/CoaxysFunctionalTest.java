package functional;

import play.Play;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.Router;
import play.test.FunctionalTest;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public abstract class CoaxysFunctionalTest extends FunctionalTest {

    protected Response login(String username, String password) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("password", password);
        Response loginResponse = POST(Router.reverse("Secure.login", params), true);
        assertStatus(200, loginResponse);
        return loginResponse;
    }

    protected Request requestAfter302(Response response) {
        Request request = newRequest();
        request.cookies = response.cookies;
        request.body = new ByteArrayInputStream(new byte[0]);
        return request;
    }

    public static Response GET(Object url, boolean followRedirect) {
        return GET(newRequest(), url, followRedirect);
    }

    public static Response GET(Request request, Object url, boolean followRedirect) {
        Response response = GET(request, url);
        if (Http.StatusCode.FOUND == response.status && followRedirect) {
            String base = Request.current() == null ? Play.configuration.getProperty("application.baseUrl", "application.baseUrl") : Request.current().getBase();
            Http.Header redirectedTo = response.headers.get("Location");
            java.net.URL redirectedUrl = null;
            try {
                redirectedUrl = new java.net.URL(base + redirectedTo.value());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            response = GET(redirectedUrl.getPath());
        }
        return response;
    }

    public static Response GET(Object url, Map<String, Http.Header> headers, boolean followRedirect) {
        Request request = Request.createRequest(null, "GET", "/", "", null, null, null, null, false, 80, "localhost", false, headers, null);
        Response response = GET(request, url);
        if (Http.StatusCode.FOUND == response.status && followRedirect) {
            String base = Request.current() == null ? Play.configuration.getProperty("application.baseUrl", "application.baseUrl") : Request.current().getBase();
            Http.Header redirectedTo = response.headers.get("Location");
            java.net.URL redirectedUrl = null;
            try {
                redirectedUrl = new java.net.URL(base + redirectedTo.value());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            response = GET(redirectedUrl.getPath());
        }
        return response;
    }

    public static Response POST(Object url, boolean followRedirect) {
        return POST(url, null, followRedirect, "");
    }

    public static Response POST(Object url, boolean followRedirect, String body) {
        return POST(url, null, followRedirect, body);
    }

    public static Response POST(Object url, Map<String, Http.Header> headers, boolean followRedirect) {
        return POST(url, headers, followRedirect, "");
    }

    public static Response POST(Object url, Map<String, Http.Header> headers, boolean followRedirect, String body) {
        Response response = null;

        if (headers != null) {
            Request request = Request.createRequest(null, "GET", "/", "", null, null, null, null, false, 80, "localhost", false, headers, null);
            response = POST(request, url, APPLICATION_X_WWW_FORM_URLENCODED, body);
        } else {
            response = POST(url, APPLICATION_X_WWW_FORM_URLENCODED, body);
        }

        if (Http.StatusCode.FOUND == response.status && followRedirect) {
            Http.Header redirectedTo = response.headers.get("Location");
            response = GET(redirectedTo.value());
        }
        return response;
    }
}