package ru.depedence.fixture;

import java.util.HashMap;
import java.util.Map;

public class NoteFixture {

    public static Map<String, Object> validCreateNoteRequest(int userId, String title) {
        Map<String, Object> request = new HashMap<>();

        request.put("userId", userId);
        request.put("title", title);

        return request;
    }

}