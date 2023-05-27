package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public File getFileById(Integer fileId) {
        return fileMapper.findById(fileId);
    }

    public void createFile(File file) {
        fileMapper.insert(file);
    }

    public List<File> getFilesByUserId(Integer userId) {
        return fileMapper.getByUserId(userId);
    }

    public void updateFile(File file) {
        fileMapper.update(file);
    }

    public void deleteFile(Integer fileId) {
        fileMapper.delete(fileId);
    }
}
