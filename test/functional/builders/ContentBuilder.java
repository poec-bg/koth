package functional.builders;

import functional.CoaxysFunctionalTest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import play.mvc.Http.Response;
import play.mvc.Router;

/**
 * <p>Builder permettant de tester la response</p>
 * <ul>
 * <li>Les Headers</li>
 * <li>Le Content Type</li>
 * <li>Le contenu HTML</li>
 * </ul>
 *
 * @author nicogiard
 */
public class ContentBuilder {

    private static ContentBuilder instance;

    public static Response response;

    private ContentBuilder(Response response) {
        this.response = response;
    }

    public static ContentBuilder create(Response response) {
        instance = new ContentBuilder(response);
        return instance;
    }

    /**
     * <tt>shouldHaveLocationOf</tt> permet de tester l'adéquation de l'action passée en paramètre avec la valeur du Header "Location"
     *
     * @param action l'action que l'on souhaite tester, sous la forme <tt>"Controller.method"</tt> (à mettre en rapport avec votre méthode de controller)
     * @return une instance d'<tt>ContentBuilder</tt> pour continuer à tester le contenu
     */
    public ContentBuilder shouldHaveLocationOf(String action) {
        shouldHaveHeaderValue("Location", Router.reverse(action).url);
        return instance;
    }

    /**
     * <tt>shouldHaveHeaderValue</tt> permet de tester la valeur d'une clé de Header
     *
     * @param key   la clé de header
     * @param value la valeur attendue
     * @return une instance d'<tt>ContentBuilder</tt> pour continuer à tester le contenu
     */
    public ContentBuilder shouldHaveHeaderValue(String key, String value) {
        if (!this.response.headers.containsKey(key)) {
            CoaxysFunctionalTest.assertFalse("la key " + key + " n'existe pas dans les headers", true);
        } else {
            CoaxysFunctionalTest.assertEquals(value, this.response.headers.get(key).value());
        }
        return instance;
    }

    /**
     * <p><tt>shouldHaveHTMLContentType</tt> permet de tester si le content type de la response est bien "text/html";</p>
     * <p>Pour mémoire : <a href="http://fr.wikipedia.org/wiki/Type_MIME">Les Types MIME</a></p>
     *
     * @return une instance d'<tt>ContentBuilder</tt> pour continuer à tester le contenu
     */
    public ContentBuilder shouldHaveHTMLContentType() {
        CoaxysFunctionalTest.assertContentType("text/html", response);
        return instance;
    }

    /**
     * <p><tt>shouldHavePDFContentType</tt> permet de tester si le content type de la response est bien "application/pdf";</p>
     * <p>Pour mémoire : <a href="http://fr.wikipedia.org/wiki/Type_MIME">Les Types MIME</a></p>
     *
     * @return une instance d'<tt>ContentBuilder</tt> pour continuer à tester le contenu
     */
    public ContentBuilder shouldHavePDFContentType() {
        CoaxysFunctionalTest.assertContentType("application/pdf", response);
        return instance;
    }

    /**
     * <p><tt>shouldHaveImageContentType</tt> permet de tester si le content type de la response commence bien par "image/";</p>
     * <p>Pour mémoire : <a href="http://fr.wikipedia.org/wiki/Type_MIME">Les Types MIME</a></p>
     *
     * @return une instance d'<tt>ContentBuilder</tt> pour continuer à tester le contenu
     */
    public ContentBuilder shouldHaveImageContentType() {
        CoaxysFunctionalTest.assertContentType("image/", response);
        return instance;
    }

    //    public ContentBuilder shouldHaveContentMatch(String pattern) {
    //        Pattern ptn = Pattern.compile(pattern);
    //        String content = CoaxysFunctionalTest.getContent(response).replaceAll("\n", "").replaceAll("\t", "").replaceAll(">[^\\w]*<", "><");
    //        boolean ok = ptn.matcher(content).find();
    //        CoaxysFunctionalTest.assertTrue("Response content does not match '" + pattern + "'", ok);
    //        return instance;
    //    }

    /**
     * <p><tt>shouldHaveTag</tt> permet de tester si un Element correspondant au selecteur passé en paramètre, existe dans la page</p>
     * <p>Utilisation de <a href="http://jsoup.org">JSoup</a></p>
     *
     * @param selector le selector jQuery / CSS style (ex : "article.left header h1")
     * @return une instance d'<tt>ContentBuilder</tt> pour continuer à tester le contenu
     */
    public ContentBuilder shouldHaveTag(String selector) {
        Document doc = Jsoup.parse(CoaxysFunctionalTest.getContent(response));
        Element element = doc.select(selector).first();
        CoaxysFunctionalTest.assertNotNull("Le tag [" + selector + "] n'a pas été trouvé", element);
        return instance;
    }

    /**
     * <p><tt>shouldHaveTag</tt> permet de tester si un Element correspondant au selecteur passé en paramètre, n'existe dans la page</p>
     * <p>Utilisation de <a href="http://jsoup.org">JSoup</a></p>
     *
     * @param selector le selector jQuery / CSS style (ex : "article.left header h1")
     * @return une instance d'<tt>ContentBuilder</tt> pour continuer à tester le contenu
     */
    public ContentBuilder shouldNotHaveTag(String selector) {
        Document doc = Jsoup.parse(CoaxysFunctionalTest.getContent(response));
        Element element = doc.select(selector).first();
        CoaxysFunctionalTest.assertNull(element);
        return instance;
    }

    /**
     * <p><tt>shouldHaveContentHtml</tt> permet de tester si le premier Element correspondant au selecteur passé en paramètre, contient exactement le code html voulu</p>
     * <p>Utilisation de <a href="http://jsoup.org">JSoup</a></p>
     *
     * @param selector le selector jQuery / CSS style (ex : "article.left header h1")
     * @param value    le contenu html à trouver (ex : "&lt;strong&gt;Titre&lt;/strong&gt;")
     * @return une instance d'<tt>ContentBuilder</tt> pour continuer à tester le contenu
     */
    public ContentBuilder shouldHaveContentHtml(String selector, String value) {
        Document doc = Jsoup.parse(CoaxysFunctionalTest.getContent(response));
        Element element = doc.select(selector).first();
        CoaxysFunctionalTest.assertNotNull("Le selector demandé [" + selector + "] n'est pas trouvé", element);
        CoaxysFunctionalTest.assertEquals(value, element.html());
        return instance;
    }

    /**
     * <p><tt>shouldHaveContentHtml</tt> permet de tester si le nième Element correspondant au selecteur passé en paramètre, contient exactement le code html voulu</p>
     * <p>Utilisation de <a href="http://jsoup.org">JSoup</a></p>
     *
     * @param selector le selector jQuery / CSS style (ex : "article.left header h1")
     * @param value    le contenu html à trouver (ex : "&lt;strong&gt;Titre&lt;/strong&gt;")
     * @param index    l'index de l'élément recherché
     * @return une instance d'<tt>ContentBuilder</tt> pour continuer à tester le contenu
     */
    public ContentBuilder shouldHaveContentHtml(String selector, String value, int index) {
        Document doc = Jsoup.parse(CoaxysFunctionalTest.getContent(response));
        Element element = doc.select(selector).get(index);
        CoaxysFunctionalTest.assertNotNull("Le selector demandé [" + selector + "] n'est pas trouvé", element);
        CoaxysFunctionalTest.assertEquals(value, element.html());
        return instance;
    }

    /**
     * <p><tt>shouldHaveContentOuterHtml</tt> permet de tester si le premier Element correspondant au selecteur passé en paramètre, contient exactement le code html voulu</p>
     * <p>Utilisation de <a href="http://jsoup.org">JSoup</a></p>
     *
     * @param selector le selector jQuery / CSS style (ex : "article.left header h1")
     * @param value    le contenu html à trouver (ex : "&lt;h1&gt;&lt;strong&gt;Titre&lt/strong&gt;&lt;/h1&gt;")
     * @return une instance d'<tt>ContentBuilder</tt> pour continuer à tester le contenu
     */
    public ContentBuilder shouldHaveContentOuterHtml(String selector, String value) {
        Document doc = Jsoup.parse(CoaxysFunctionalTest.getContent(response));
        Element element = doc.select(selector).first();
        CoaxysFunctionalTest.assertNotNull("Le selector demandé [" + selector + "] n'est pas trouvé", element);
        CoaxysFunctionalTest.assertEquals(value, element.outerHtml());
        return instance;
    }

    /**
     * <p><tt>shouldHaveContentOuterHtml</tt> permet de tester si le nième Element correspondant au selecteur passé en paramètre, contient exactement le code html voulu</p>
     * <p>Utilisation de <a href="http://jsoup.org">JSoup</a></p>
     *
     * @param selector le selector jQuery / CSS style (ex : "article.left header h1")
     * @param value    le contenu html à trouver (ex : "&lt;h1&gt;&lt;strong&gt;Titre&lt/strong&gt;&lt;/h1&gt;")
     * @return une instance d'<tt>ContentBuilder</tt> pour continuer à tester le contenu
     */
    public ContentBuilder shouldHaveContentOuterHtml(String selector, String value, int index) {
        Document doc = Jsoup.parse(CoaxysFunctionalTest.getContent(response));
        Element element = doc.select(selector).get(index);
        CoaxysFunctionalTest.assertNotNull("Le selector demandé [" + selector + "] n'est pas trouvé", element);
        CoaxysFunctionalTest.assertEquals(value, element.outerHtml());
        return instance;
    }

    /**
     * <p><tt>shouldHaveContentText</tt> permet de tester si le premier Element correspondant au selecteur passé en paramètre, contient exactement le texte voulu (sans décoration html)</p>
     * <p>Utilisation de <a href="http://jsoup.org">JSoup</a></p>
     *
     * @param selector le selector jQuery / CSS style (ex : "article.left header h1")
     * @param value    le texte à trouver (ex : "Titre")
     * @return une instance d'<tt>ContentBuilder</tt> pour continuer à tester le contenu
     */
    public ContentBuilder shouldHaveContentText(String selector, String value) {
        Document doc = Jsoup.parse(CoaxysFunctionalTest.getContent(response));
        Element element = doc.select(selector).first();
        CoaxysFunctionalTest.assertNotNull("Le selector demandé [" + selector + "] n'est pas trouvé", element);
        CoaxysFunctionalTest.assertEquals(value, element.text());
        return instance;
    }

    /**
     * <p><tt>shouldHaveContentText</tt> permet de tester si le nième Element correspondant au selecteur passé en paramètre, contient exactement le texte voulu (sans décoration html)</p>
     * <p>Utilisation de <a href="http://jsoup.org">JSoup</a></p>
     *
     * @param selector le selector jQuery / CSS style (ex : "article.left header h1")
     * @param value    le texte à trouver (ex : "Titre")
     * @param index    l'index de l'élément recherché
     * @return une instance d'<tt>ContentBuilder</tt> pour continuer à tester le contenu
     */
    public ContentBuilder shouldHaveContentText(String selector, String value, int index) {
        Document doc = Jsoup.parse(CoaxysFunctionalTest.getContent(response));
        Element element = doc.select(selector).get(index);
        CoaxysFunctionalTest.assertNotNull("Le selector demandé [" + selector + "] n'est pas trouvé", element);
        CoaxysFunctionalTest.assertEquals(value, element.text());
        return instance;
    }

    /**
     * <p><tt>shouldHaveTagAttribute</tt> permet de tester si le premier Element correspondant au selecteur passé en paramètre, contient exactement l'attibut html voulu</p>
     * <p>Utilisation de <a href="http://jsoup.org">JSoup</a></p>
     *
     * @param selector le selector jQuery / CSS style (ex : "nav a#acceil")
     * @param tag      le tag recherché (ex: "href")
     * @param value    le contenu html à trouver (ex : "http://monsite.com")
     * @return une instance d'<tt>ContentBuilder</tt> pour continuer à tester le contenu
     */
    public ContentBuilder shouldHaveTagAttribute(String selector, String tag, String value) {
        Document doc = Jsoup.parse(CoaxysFunctionalTest.getContent(response));
        Element element = doc.select(selector).first();
        CoaxysFunctionalTest.assertNotNull("Le selector demandé [" + selector + "] n'est pas trouvé", element);
        CoaxysFunctionalTest.assertEquals(value, element.attr(tag));
        return instance;
    }

    /**
     * <p><tt>shouldHaveTagAttribute</tt> permet de tester si le nième Element correspondant au selecteur passé en paramètre, contient exactement l'attibut html voulu</p>
     * <p>Utilisation de <a href="http://jsoup.org">JSoup</a></p>
     *
     * @param selector le selector jQuery / CSS style (ex : "nav a#acceil")
     * @param tag      le tag recherché (ex: "href")
     * @param value    le contenu html à trouver (ex : "http://monsite.com")
     * @return une instance d'<tt>ContentBuilder</tt> pour continuer à tester le contenu
     */
    public ContentBuilder shouldHaveTagAttribute(String selector, String tag, String value, int index) {
        Document doc = Jsoup.parse(CoaxysFunctionalTest.getContent(response));
        Element element = doc.select(selector).get(index);
        CoaxysFunctionalTest.assertNotNull("Le selector demandé [" + selector + "] n'est pas trouvé", element);
        CoaxysFunctionalTest.assertEquals(value, element.attr(tag));
        return instance;
    }

    /**
     * <p><tt></tt> permet de tester si le premier Element correspondant au selecteur passé en paramètre, contient exactement le nombre de sous éléments voulu</p>
     *
     * @param selector le selector jQuery / CSS style (ex : "nav")
     * @param count    le nombre de sous éléments souhaité
     * @return une instance d'<tt>ContentBuilder</tt> pour continuer à tester le contenu
     */
    public ContentBuilder shouldHaveContentListCount(String selector, int count) {
        Document doc = Jsoup.parse(CoaxysFunctionalTest.getContent(response));
        Element element = doc.select(selector).first();
        CoaxysFunctionalTest.assertNotNull("Le selector demandé [" + selector + "] n'est pas trouvé", element);
        CoaxysFunctionalTest.assertEquals(count, element.children().size());
        return instance;
    }
}