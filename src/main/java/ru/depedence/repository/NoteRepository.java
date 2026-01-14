package ru.depedence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.depedence.entity.Note;
import ru.depedence.entity.NoteStatus;

@Repository
@Transactional
public interface NoteRepository extends JpaRepository<Note, Integer> {

    @Modifying
    @Query("UPDATE Note SET status = :status WHERE id = :id")
    void update(int id, @Param("status") NoteStatus newStatus);

}