package com.acme.marsrover.service;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public interface FileReaderService {
    Scanner getScanner(String filePath);
    Scanner getScanner(File file);
    Scanner getScannerForDatesTextFile();
    List<String> getDatesFromTextFile();
}
