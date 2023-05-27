package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Insert("INSERT INTO NOTES (noteTitle, noteDescription, userId) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int create(Note note);

    @Select("SELECT * FROM NOTES WHERE noteId = #{noteId}")
    Note findById(Integer noteId);

    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    List<Note> findByUserId(Integer userId);

    @Select("SELECT * FROM NOTES")
    List<Note> findAllNotes();

    @Update("UPDATE NOTES SET noteTitle = #{noteTitle},noteDescription = #{noteDescription} WHERE noteId = #{noteId}")
    void update(Note note);

    @Delete("DELETE FROM NOTES where noteId = #{noteId}")
    void delete(Integer noteId);
}
