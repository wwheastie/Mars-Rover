package com.acme.marsrover.service;

import com.acme.marsrover.service.impl.FileReaderServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ResourceUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.Assert.assertNotNull;

public class FileReaderServiceImplTest {
    private FileReaderService fileReaderService;

    @Before
    public void setUp() {
        fileReaderService = new FileReaderServiceImpl();
    }

    @After
    public void tearDown() {
        fileReaderService = null;
    }

    @Test(expected = ResponseStatusException.class)
    public void testBadFilePath() {
        String badFilePath = "9:\\badPath\\";
        fileReaderService.getScanner(badFilePath);
    }

    @Test(expected = Exception.class)
    public void testFileDoesNotExist() {
        String nonExistentFile = getClass().getClassLoader().getResource("test.txt").getPath();
        fileReaderService.getScanner(nonExistentFile);
    }

    @Test
    public void testDatesTextFileExists() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:dates.txt");
        assertNotNull(file);
    }

    @Test
    public void testGetScannerForDatesFileIsNotNull() {
        Scanner scanner = fileReaderService.getScannerForDatesTextFile();
        assertNotNull(scanner);
    }

}
