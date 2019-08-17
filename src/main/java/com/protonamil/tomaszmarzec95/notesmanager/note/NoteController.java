package com.protonamil.tomaszmarzec95.notesmanager.note;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.protonamil.tomaszmarzec95.notesmanager.user.User;
import com.protonamil.tomaszmarzec95.notesmanager.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/notes/")
public class NoteController {

    private NoteRepository noteRepository;
    private ObjectMapper objectMapper = new ObjectMapper();
    private UserService userService;


    public NoteController(NoteRepository noteRepository, UserService userService) {
        this.noteRepository = noteRepository;
        this.userService = userService;
    }

 //   @CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> createNote(@Valid @RequestBody Note note, BindingResult result,
                                             @RequestHeader("Authorization") String tokenHeader){
        if(result.hasErrors()) {
            try{
                return new ResponseEntity<>(objectMapper.writeValueAsString(result.getAllErrors()),
                        HttpStatus.BAD_REQUEST);
            }catch (JsonProcessingException e) {
                System.out.println(e);
            }
        }

        User user = userService.getUserByTokenHeader(tokenHeader);
        note.setNoteUser(user);
        noteRepository.save(note);

        return new ResponseEntity<>("note created", HttpStatus.CREATED);
    }


    @GetMapping(path = "/getall", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Note>> getAllUserNotes(@RequestHeader("Authorization") String tokenHeader) {
        User user = userService.getUserByTokenHeader(tokenHeader);
        List<Note> noteList=
                noteRepository.findAllByNoteUser(user);
        return new ResponseEntity<>(noteList, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> deleteNote(@Valid @RequestBody NoteDto noteDto,
                                             @RequestHeader("Authorization") String tokenHeader){


        Note note = noteRepository.findById(noteDto.getId()).orElse(null);
        if (note == null) {
            return new ResponseEntity<>("note not found", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        User user = userService.getUserByTokenHeader(tokenHeader);
        if(!user.getId().equals(note.getNoteUser().getId())) {
            if(!note.getNoteUser().getId().equals(user.getId())) {
                return new ResponseEntity<>("note not accessible for user", HttpStatus.UNAUTHORIZED);
            }
        }

        noteRepository.delete(note);

        return new ResponseEntity<>("note deleted", HttpStatus.OK);
    }

    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> editNote(@Valid @RequestBody NoteDto noteDto,
                                              BindingResult result,
                                              @RequestHeader("Authorization") String tokenHeader){
        if(result.hasErrors()) {
            try{
                return new ResponseEntity<>(objectMapper.writeValueAsString(result.getAllErrors()),
                        HttpStatus.BAD_REQUEST);
            }catch (JsonProcessingException e) {
                System.out.println(e);
            }
        }

        Long id = noteDto.getId();
        User user = userService.getUserByTokenHeader(tokenHeader);
        Note note = noteRepository.findById(id).orElse(null);

        if(note == null) {
            return new ResponseEntity<>("note not found", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if(!note.getNoteUser().getId().equals(user.getId())) {
            return new ResponseEntity<>("note not accessible for user", HttpStatus.UNAUTHORIZED);
        }

        String newTitle = noteDto.getTitle();
        String newText = noteDto.getText();

        if(newText != null && !newText.isEmpty()) {
            note.setText(newText);
        }

        if(newTitle != null && !newTitle.isEmpty()) {
            note.setTitle(newTitle);
        }

        note.setUpdatedOn(LocalDateTime.now());

        noteRepository.save(note);
        return new ResponseEntity<>("note edited", HttpStatus.OK);
    }

}
