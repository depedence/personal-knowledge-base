package ru.depedence.fixture;

import ru.depedence.repository.NoteCategory;

import java.util.HashMap;
import java.util.Map;

public class NoteFixture {

    public static Map<String, Object> validCreateNoteRequest(int userId, String title, String content, NoteCategory category) {
        Map<String, Object> request = new HashMap<>();

        request.put("userId", userId);
        request.put("title", title);
        request.put("content", content);
        request.put("category", category);

        return request;
    }

    public static Map<String, Object> validEditNoteRequest(String title, String content, NoteCategory category, int userId) {
        Map<String, Object> request = new HashMap<>();

        request.put("title", title);
        request.put("content", content);
        request.put("category", category);
        request.put("userId", userId);

        return request;
    }

}