package com.learning.RandomPosts.service;

import com.learning.RandomPosts.exception.StorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public abstract class StorageService {

    protected Path STORAGE_LOCATION;

    void init(){
        if(Files.exists(STORAGE_LOCATION)){
            return;
        }
        try {
            Files.createDirectories(STORAGE_LOCATION);
        } catch(IOException exception){
            throw new StorageException("Could not initialize storage.", exception);
        }
    }

    public void store(List<MultipartFile> files, String folderUUID) {
        if(files.isEmpty() || files.size() > 5){
            throw new StorageException("Could not proceed with the request. Please check the attachment quantity.");
        }
        if(Files.notExists(STORAGE_LOCATION.resolve(folderUUID))){
            try{
                Files.createDirectory(STORAGE_LOCATION.resolve(folderUUID));
            } catch(IOException exception){
                throw new StorageException("Could not create directory for file.", exception);
            }
        }

        for(MultipartFile file : files){
            try {
                Files.copy(file.getInputStream(), STORAGE_LOCATION.resolve(folderUUID).resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException exception) {
                throw new StorageException("Could not create directory for file.", exception);
            }
        }
    }

    void deleteFile(String filePrefix, String fileName){
        try {
            Files.deleteIfExists(STORAGE_LOCATION.resolve(filePrefix).resolve(fileName));
        } catch (IOException exception) {
            throw new StorageException("Could not delete file.", exception);
        }
    }

    void deleteAll(String path) {
        try {
            Files.deleteIfExists(STORAGE_LOCATION.resolve(path));
        } catch (IOException exception) {
            throw new StorageException("Could not delete all files.", exception);
        }
    }

}
