package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper { //Mapper is interface not class
    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File findById(Integer fileId);

    @Insert("INSERT INTO FILES (fileName, contentType, fileSize, userId, fileData) VALUES (#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    void insert(File file);

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getByUserId(Integer userId);

    @Update("UPDATE FILES SET fileName = #{fileName}, contentType = #{contentType}, " +
            "fileSize = #{fileSize}, userId = #{userId}, fileData = #{fileData} WHERE fileId = #{fileId}")
    void update(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    void delete(Integer fileId);
}
