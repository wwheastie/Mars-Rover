package com.acme.marsrover.service.impl;

import com.acme.marsrover.service.FileReaderService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class FileReaderServiceImpl extends BaseService implements FileReaderService {
    /**
     * Retrieve scanner for dates file
     *
     * @return
     */
    public Scanner getScannerForDatesTextFile() {
        //Get dates text file
        File file;

        try {
            file = ResourceUtils.getFile("classpath:dates.txt");
        } catch (FileNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not find dates text file", e);
        }

        //Create new scanner for dates text file
        return getScanner(file);
    }

    /**
     * Retrieves dates as a list of Strings from dates.txt file using it as an input stream
     *
     * @return
     */
    public List<String> getDatesFromTextFile() {
        //Get dates file as an input stream
        InputStream inputStream = getDatesTextFileInputStream();

        //Return list of dates
        return writeInputStreamToStringList(inputStream);
    }

    /**
     * Returns a list of strings reading from an input stream
     *
     * @param inputStream
     * @return
     */
    private List<String> writeInputStreamToStringList(InputStream inputStream) {
        List<String> listOfStrings = new ArrayList<>();
        try {
            InputStreamReader isReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(isReader);
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                listOfStrings.add(string);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error while converting input stream to list of strings", e);
        }
        return listOfStrings;
    }

    /**
     * Creates and returns Scanner object from String file path
     *
     * @param filePath
     * @return
     */
    @Override
    public Scanner getScanner(String filePath) {
        //Create uri from filePath
        URI uri = createURI(filePath);

        //Create file from uri
        File file = createFile(uri);

        //Create scanner from file
        Scanner scanner = createScanner(file);

        //Return scanner
        return scanner;
    }

    /**
     * Creates and returns Scanner object from File object
     *
     * @param file
     * @return
     */
    @Override
    public Scanner getScanner(File file) {
        //Create scanner from file
        Scanner scanner = createScanner(file);

        //Return scanner
        return scanner;
    }

    /**
     * Creates Scanner object using File Object
     *
     * @param file
     * @return
     */
    private Scanner createScanner(File file) {
        Scanner scanner;
        try {
            logger.info("Creating scanner for " + file.getAbsolutePath());
            scanner = new Scanner(file);
            logger.info("Scanner created successfully");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error while creating scanner", e);
        }
        return scanner;
    }

    /**
     * Creates File object using URI object
     *
     * @param uri
     * @return
     */
    private File createFile(URI uri) {
        File file;
        try {
            logger.info("Creating file for " + uri.getPath());
            file = new File(uri);
            logger.info("File created successfully");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error while creating file", e);
        }
        return file;
    }


    /**
     * Creates an URI object from a String file path
     *
     * @param filePath
     * @return
     */
    private URI createURI(String filePath) {
        URI uri;
        try {
            logger.info("Creating URI for " + filePath);
            uri = URI.create(filePath);
            logger.info("URI created successfully");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error while converting file path string to URI", e);
        }
        return uri;
    }

    /**
     * Retrieve file path as String for dates file
     *
     * @return
     */
    private String getDatesTextFilePath() {
        logger.info("Getting path for " + datesFileName);
        String filePath = getClass().getClassLoader().getResource(datesFileName).toString();
        logger.info("File path retrieved successfully: " + filePath);
        return filePath;
    }

    /**
     * Reads the dates text file as an input stream
     *
     * @return
     */
    private InputStream getDatesTextFileInputStream() {
        logger.info("Getting input stream of dates text file");
        InputStream inputStream = FileReaderServiceImpl.class.getClassLoader().getResourceAsStream("dates.txt");
        logger.info("Input stream retrieved");
        return inputStream;
    }
}
