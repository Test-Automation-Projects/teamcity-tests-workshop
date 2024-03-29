package api;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import org.workshop.api.models.ParentProject;
import org.workshop.api.models.Project;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class BuildConfigurationTest extends BaseTest {
    @Test
    public void buildConfigurationTest() {
        // 0. Get CSRF token
        var csrfToken = RestAssured.get("http://admin:admin@localhost:8111/authenticationTest.html?csrf")
                .then().extract().response().asString();

        // Specifications
        var requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri("http://admin:admin@localhost:8111");
        requestSpecBuilder.setContentType(ContentType.JSON);
        requestSpecBuilder.setAccept(ContentType.JSON);
        requestSpecBuilder.addHeader("X-TC-CSRF-Token", csrfToken);

        var reqSpec = requestSpecBuilder.build();

        // Create project object
        var project = Project.builder()
                .parentProject(
                        ParentProject.builder().locator("_Root").build()
                )
                .name("Generated project1")
                .id("GeneratedProjectId1")
                .copyAllAssociatedSettings(true)
                .build();

        var projectJson = new Gson().toJson(project);

        // POST request: create project
        given().spec(reqSpec).body(projectJson).post("/app/rest/projects")
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .body("id", equalTo("GeneratedProjectId1"));
    }
}
