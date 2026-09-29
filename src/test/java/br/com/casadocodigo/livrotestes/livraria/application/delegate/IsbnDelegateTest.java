package br.com.casadocodigo.livrotestes.livraria.application.delegate;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.ArrayMatching.asEqualMatchers;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IsbnDelegateTest {

    private static final String URL = "https://openlibrary.org/isbn/{NUMBER}.json";

    private static final String ISBN = "9788555192135";

    // Teste de integração com a API externa
    @Test
    public void testObterLivro() {

        given()
                .pathParams("NUMBER", ISBN)
                .contentType(ContentType.JSON)
                .when().get(URL).then()
                .assertThat().statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("title", equalTo("Orientação a Objetos: Aprenda seus conceitos e suas aplicabilidades de forma efetiva"))
                    .body("isbn_13", contains(asEqualMatchers(new String[] {"9788555192135"})));
    }
}
