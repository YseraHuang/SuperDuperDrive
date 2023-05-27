package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int createNote(Note note){
        return noteMapper.create(note);
    }

    public List<Note> getAllUserNotes(Integer userId){
        return noteMapper.findByUserId(userId);
    }

    public List<Note> getAllNotes(){
        return noteMapper.findAllNotes();
    }

    public void deleteNote(Integer noteId){
        noteMapper.delete(noteId);
    }

    public void updateNote(Note note){
        noteMapper.update(note);
    }

    public Note findNote(Integer noteId){
        return noteMapper.findById(noteId);
    }
}
