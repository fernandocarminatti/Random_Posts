package com.learning.RandomPosts.service;

import com.learning.RandomPosts.exception.InternalStorageException;
import com.learning.RandomPosts.exception.StorageException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileSystemStorageServiceTest {

    @Mock
    Path dummyPath;
    @InjectMocks
    FileSystemStorageService fileSystemStorageService;

    @DisplayName("Create folder for FileSystemStorageService")
    @Test
    void init_StorageLocationExists_DoNothing() {
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)){
            filesMock.when(() -> Files.exists(fileSystemStorageService.getStorageLocation())).thenReturn(true);

            fileSystemStorageService.init();

            filesMock.verify(() -> Files.exists(fileSystemStorageService.getStorageLocation()));
            filesMock.verify(() -> Files.createDirectories(fileSystemStorageService.getStorageLocation()), never());
        }
    }

    @DisplayName("Does not create folder for FileSystemStorageService")
    @Test
    void init_StorageLocationDoesNotExists_CreateFolder() {
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)){
            filesMock.when(() -> Files.exists(fileSystemStorageService.getStorageLocation())).thenReturn(false);
            filesMock.when(() -> Files.createDirectories(fileSystemStorageService.getStorageLocation())).thenAnswer(invocation -> invocation.getArgument(0));

            fileSystemStorageService.init();

            filesMock.verify(() -> Files.exists(fileSystemStorageService.getStorageLocation()));
            filesMock.verify(() -> Files.createDirectories(fileSystemStorageService.getStorageLocation()));
        }
    }

    @DisplayName("Throws Internal Storage Exception")
    @Test
    void init_CreateFolderThrowsException_InternalStorageException() {
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)){
            filesMock.when(() -> Files.exists(fileSystemStorageService.getStorageLocation())).thenReturn(false);
            filesMock.when(() -> Files.createDirectories(fileSystemStorageService.getStorageLocation())).thenThrow(new IOException());

            InternalStorageException exception = assertThrows(InternalStorageException.class, () -> fileSystemStorageService.init());

            assertNotNull(exception.getCause());
            assertInstanceOf(IOException.class, exception.getCause());
            assertEquals("Could not initialize storage.", exception.getMessage());

            filesMock.verify(() -> Files.exists(fileSystemStorageService.getStorageLocation()));
            filesMock.verify(() -> Files.createDirectories(fileSystemStorageService.getStorageLocation()));
        }
    }

    @DisplayName("Throws Internal Storage Exception | Empty List of Files")
    @Test
    void store_EmptyListOfFilesThrowsException_StorageException() {
        List<MultipartFile> dummyEmptyList = mock(List.class);
        when(dummyEmptyList.isEmpty()).thenReturn(true);

        assertThrows(StorageException.class, () -> fileSystemStorageService.store(dummyEmptyList, "test"));
    }

    @DisplayName("Throws StorageException | Wrong files format")
    @Test
    void store_CouldNotValidateFilesFormat_StorageException() {
        MultipartFile dummyFile001 = new MockMultipartFile("file1", "test1.txt", "text/plain", new byte[0]);
        MultipartFile dummyFile002 = new MockMultipartFile("file2", "test2.jpg", "image/jpeg", new byte[0]);
        List<MultipartFile> dummyList = Arrays.asList(dummyFile001, dummyFile002);

        assertThrows(StorageException.class, () -> fileSystemStorageService.store(dummyList, "test"));
    }

    @Test
    void deleteFile() {
    }

    @Test
    void deleteAll() {
    }

    @Test
    void validateFileFormat(List<MultipartFile> dummyFilesList) {
    }
}