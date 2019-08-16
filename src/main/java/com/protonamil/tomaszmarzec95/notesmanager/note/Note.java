package com.protonamil.tomaszmarzec95.notesmanager.note;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.protonamil.tomaszmarzec95.notesmanager.user.User;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Transactional
public class Note {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    @Size(max = 100)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String text;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User noteUser;

    @PrePersist @JsonIgnore
    public void prePersist() {
        createdOn = LocalDateTime.now();
    }

    @PreUpdate @JsonIgnore
    public void preUpdate() {
        updatedOn = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getNoteUser() {
        return noteUser;
    }

    public void setNoteUser(User noteUser) {
        this.noteUser = noteUser;
    }

}
