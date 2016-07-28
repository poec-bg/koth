package functional;

import org.junit.Test;

import static functional.builders.AfterLoginBuilder.withNoLogin;

public class ApplicationTest extends CoaxysFunctionalTest {

    @Test
    public void test_that_index_works(){
        withNoLogin().goTo("Application.index").shouldBeOK();
    }

    @Test
    public void test_that_product_works(){
        withNoLogin().withParam("slug", "product").goTo("Application.product").shouldBeOK();
    }

    @Test
    public void test_that_productGallery_works(){
        withNoLogin().withParam("slug", "product").goTo("Application.productGallery").shouldBeOK();
    }

    @Test
    public void test_that_productSpecifications_works(){
        withNoLogin().withParam("slug", "product").goTo("Application.productSpecifications").shouldBeOK();
    }

    @Test
    public void test_that_productAccessories_works(){
        withNoLogin().withParam("slug", "product").goTo("Application.productAccessories").shouldBeOK();
    }

    @Test
    public void test_that_productComments_works(){
        withNoLogin().withParam("slug", "product").goTo("Application.productComments").shouldBeOK();
    }
}