package tasks.apitest;

import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;

public class APITest {

    @BeforeClass
    public static void setup() {
        baseURI = "http://localhost:8001/tasks-backend";
    }

    @Test
    public void deveRetornarTasks() {
        given()
        .when()
            .get("/todo")
        .then()
            .statusCode(200)
        ;
    }

    @Test
    public void deveAdicionarTaskComSucesso() {
        given()
            .body("{ \"task\": \"Teste via API\", \"dueDate\": \"2030-12-30\" }")
            .contentType(ContentType.JSON)
        .when()
            .post("/todo")
        .then()
            .statusCode(201)
        ;
    }

    @Test
    public void naoDeveAdicionarTaskInvalida() {
        given()
            .body("{ \"task\": \"Teste via API\", \"dueDate\": \"2010-12-30\" }")
            .contentType(ContentType.JSON)
        .when()
            .post("/todo")
        .then()
            .statusCode(400)
            .body("message", is("Due date must not be in past"))
        ;
    }

    @Test
    public void deveRemoverTaskComSucesso() {
        // Inserir task para poder remover
        Integer id = given()
            .body("{ \"task\": \"Teste teste\", \"dueDate\": \"2030-12-30\" }")
            .contentType(ContentType.JSON)
        .when()
            .post("/todo")
        .then()
            .statusCode(201)
            .extract().path("id")
        ;

        // Remover task
        given()
        .when()
            .delete("/todo/" + id)
        .then()
            .statusCode(204)
        ;
    }
}
