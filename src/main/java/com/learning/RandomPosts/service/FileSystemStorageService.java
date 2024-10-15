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
import java.util.Random;

@Service
public class FileSystemStorageService implements StorageService{

    private static Path STORAGE_LOCATION;
    private final List<String> VALID_FILE_FORMATS = List.of(".pdf", ".jpg");

    public FileSystemStorageService(){
        STORAGE_LOCATION = Path.of("StorageFolder");
    }

    public void setCustomStorageLocation(String customStorageLocation){
        STORAGE_LOCATION = Path.of(customStorageLocation);
    }

    public Path getStorageLocation(){
        return STORAGE_LOCATION;
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

        String fallbackFileName = "FallbackFileName - " + Math.random();
        validateFileFormat(files);
        Path postLocation = validateFolder(folderUUID);

        for(MultipartFile file : files){
            try {
                String fileName = Objects.requireNonNullElse(file.getOriginalFilename(), fallbackFileName);
                Files.copy(file.getInputStream(), postLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException exception) {
                throw new InternalStorageException("Could not save files no disk.", exception);
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

    public boolean validateFileFormat(List<MultipartFile> fileList) {
        if(fileList.isEmpty()){
            throw new StorageException("Could not proceed with the request. Please check the attachment quantity.");
        }
        int validExtensions = 0;
        for(MultipartFile file: fileList){
            String fileExtension = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
            if( !VALID_FILE_FORMATS.contains(fileExtension) ){
                throw new StorageException("Could not proceed with the request. Please check attachments format. Only .pdf and .jpg are allowed.");
            }
            validExtensions++;
        }
        return validExtensions == fileList.size();
    }

    public Path validateFolder(String folderUUID) {
        if(Files.exists(STORAGE_LOCATION.resolve(folderUUID))){
            try {
                return Files.createDirectory(STORAGE_LOCATION.resolve(folderUUID));
            } catch(IOException exception){
                throw new InternalStorageException("Could not create directory", exception);
            }
        }
        try{
            return Files.createDirectory(STORAGE_LOCATION.resolve(folderUUID));
        } catch(IOException exception){
            throw new InternalStorageException("Could not create directory for file.", exception);
        }
    }

}
