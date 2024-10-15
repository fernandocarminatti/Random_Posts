package com.learning.RandomPosts.service;

import com.learning.RandomPosts.exception.InternalStorageException;
import com.learning.RandomPosts.exception.StorageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
public class FileSystemStorageService implements StorageService{

    protected Path STORAGE_LOCATION;
    protected List<String> VALID_FILE_FORMATS = List.of(".pdf", ".jpg");

    public FileSystemStorageService(){
        this.STORAGE_LOCATION = Path.of("StorageFolder");
        this.init();
    }

    @Override
    public void init() {
        if(Files.exists(STORAGE_LOCATION)){
            return;
        }
        try {
            Files.createDirectories(STORAGE_LOCATION);
        } catch(IOException exception){
            throw new InternalStorageException("Could not initialize storage.", exception);
        }
    }

    @Override
    public void store(List<MultipartFile> files, String folderUUID) {
        if(files.isEmpty()){
            throw new StorageException("Could not proceed with the request. Please check the attachment quantity.");
        }
        if( !validateFileFormat(files) ){
            throw new StorageException("File format(s) not supported. Accepted values are .pdf and .jpg");
        }
        if(Files.notExists(STORAGE_LOCATION.resolve(folderUUID))){
            try{
                Files.createDirectory(STORAGE_LOCATION.resolve(folderUUID));
            } catch(IOException exception){
                throw new InternalStorageException("Could not create directory for file.", exception);
            }
        }
        for(MultipartFile file : files){
            try {
                Files.copy(file.getInputStream(), STORAGE_LOCATION.resolve(folderUUID).resolve(Objects.requireNonNull(file.getOriginalFilename())), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException exception) {
                throw new InternalStorageException("Could not create directory for file.", exception);
            }
        }
    }

    @Override
    public void deleteFile(String folderUUID, String fileName) {
        try {
            Files.deleteIfExists(STORAGE_LOCATION.resolve(folderUUID).resolve(fileName));
        } catch (IOException exception) {
            throw new InternalStorageException("Could not delete file.", exception);
        }
    }

    @Override
    public void deleteAll(String path) {
        try {
            List<Path> filesWithinParameter = Files.list(STORAGE_LOCATION.resolve(path)).toList();
            for (Path file: filesWithinParameter){
                Files.delete(file);
            }
            Files.deleteIfExists(STORAGE_LOCATION.resolve(path));
        } catch (IOException exception) {
            throw new InternalStorageException("Could not delete all files.", exception);
        }
    }

    @Override
    public boolean validateFileFormat(List<MultipartFile> fileList) {
        int validExtensions = 0;
        for(MultipartFile file: fileList){
            String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            if(VALID_FILE_FORMATS.contains(fileExtension)){
                validExtensions++;
            }
        }
        return validExtensions == fileList.size();
    }
}
