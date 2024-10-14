package com.learning.RandomPosts.service;

import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class FileSystemStorageService extends StorageService{
    public FileSystemStorageService(){
        this.STORAGE_LOCATION = Path.of("StorageFolder");
        this.init();
    }
}
