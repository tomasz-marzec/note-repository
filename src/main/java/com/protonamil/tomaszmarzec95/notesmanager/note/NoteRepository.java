package com.protonamil.tomaszmarzec95.notesmanager.note;


import com.protonamil.tomaszmarzec95.notesmanager.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface NoteRepository extends CrudRepository<Note, Long> {
    List<Note> findAllByNoteUser(User user);

}
