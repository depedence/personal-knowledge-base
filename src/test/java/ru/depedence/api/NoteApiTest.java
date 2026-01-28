package ru.depedence.api;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.depedence.base.BaseApiTest;
import ru.depedence.entity.Note;
import ru.depedence.entity.User;
import ru.depedence.fixture.NoteFixture;
import ru.depedence.helpers.TestDataHelper;
import ru.depedence.repository.NoteRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Note API Tests")
class NoteApiTest extends BaseApiTest {

    @Autowired
    private TestDataHelper dataHelper;

    @Autowired
    private NoteRepository noteRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        dataHelper.cleanDatabase();
        testUser = dataHelper.createTestUser("testUser", "testPassword");
        authenticateAs("testUser", "testPassword");
    }

    @Test
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

    @Test
    @DisplayName("GET /api/notes - get all notes")
    void getAllNotes_Success() {
        Note testNote = dataHelper.createTestNote("testNote", testUser);

        Response response = given().spec(requestSpec)
                .when().get("/api/notes")
                .then().statusCode(200)
                .body("notes", notNullValue())
                .body("notes[0].id",notNullValue())
                .body("notes[0].title", equalTo("testNote"))
                .body("notes[0].creationDate", notNullValue())
                .body("notes[0].userId", equalTo(testUser.getId()))
                .extract().response();

        int noteId = response.jsonPath().getInt("notes[0].id");
        assertTrue(noteRepository.existsById(noteId), "Note should exist in database");

        assertEquals("testNote", testNote.getTitle());
        assertEquals(testUser.getId(), testNote.getUser().getId());
    }

    @Test
    @DisplayName("PUT /api/notes/{noteId} - edit note")
    void editNote_Success() {
        Note testNote = dataHelper.createTestNote("It's a not test note", testUser);
        var requestBody = NoteFixture.validEditNoteRequest("It's a test note bro");

        Response response = given().spec(requestSpec).body(requestBody)
                .when().put("/api/notes/" + testNote.getId())
                .then().statusCode(200)
                .body("id", notNullValue())
                .body("title", equalTo("It's a test note bro"))
                .body("creationDate", notNullValue())
                .body("userId", equalTo(testUser.getId()))
                .extract().response();

        int noteId = response.jsonPath().getInt("id");
        assertTrue(noteRepository.existsById(noteId), "Note should exist in database");

        Note editedNote = noteRepository.findById(noteId).orElseThrow();
        assertEquals("It's a test note bro", editedNote.getTitle());
        assertEquals(testUser.getId(), editedNote.getUser().getId());
    }

    @Test
    @DisplayName("DELETE /api/notes/{noteId} - delete note")
    void deleteNote_Success() {
        Note testNote = dataHelper.createTestNote("Temp Note for delete", testUser);
        int noteId = testNote.getId();

        Response response = given().spec(requestSpec)
                .when().delete("/api/notes/" + noteId)
                .then().statusCode(200)
                .extract().response();

        assertFalse(noteRepository.existsById(noteId));
    }

}