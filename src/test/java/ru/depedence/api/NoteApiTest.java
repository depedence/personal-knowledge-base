package ru.depedence.api;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.depedence.base.BaseApiTest;
import ru.depedence.entity.Note;
import ru.depedence.entity.User;
import ru.depedence.fixture.NoteFixture;
import ru.depedence.helper.TestDataHelper;
import ru.depedence.repository.NoteRepository;
import ru.depedence.repository.UserRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Note API Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NoteApiTest extends BaseApiTest {

    @Autowired
    private TestDataHelper helper;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        helper.cleanDatabase();
        testUser = helper.createTestUser("testUser", "testPassword");
    }

    @Test
    @Order(1)
    @DisplayName("POST /api/notes - create note with valid data")
    void createValidNote_Success() {
        var requestBody = NoteFixture.validCreateNoteRequest(testUser.getId(), "My first Note");

        Response response = given().spec(requestSpec).body(requestBody)
                .when().post("/api/notes")
                .then().statusCode(200)
                .body("id", notNullValue())
                .body("title", equalTo("My first Note"))
                .body("creationDate", notNullValue())
                .body("userId", equalTo(testUser.getId()))
                .extract().response();

        int noteId = response.jsonPath().getInt("id");
        assertTrue(noteRepository.existsById(noteId), "Note should exist in database");

        Note savedNote = noteRepository.findById(noteId).orElseThrow();
        assertEquals("My first Note", savedNote.getTitle());
        assertEquals(testUser.getId(), savedNote.getUser().getId());
    }

}