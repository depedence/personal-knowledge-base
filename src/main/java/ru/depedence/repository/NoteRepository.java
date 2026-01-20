package ru.depedence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.depedence.entity.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {}