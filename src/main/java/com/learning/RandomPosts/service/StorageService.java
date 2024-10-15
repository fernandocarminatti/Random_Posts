package com.learning.RandomPosts.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {

    void init();

    void store(List<MultipartFile> files, String folderUUID);

    void deleteFile(String folderUUID, String fileName);

    void deleteAll(String path);

    boolean validateFileFormat(List<MultipartFile> fileList);

}
