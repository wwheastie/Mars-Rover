package com.acme.marsrover.service.impl;

import com.acme.marsrover.dto.response.NasaApiPhotosResponse;
import com.acme.marsrover.persistence.entity.CameraEntity;
import com.acme.marsrover.persistence.entity.ImageDetailEntity;
import com.acme.marsrover.persistence.repository.ImageDetailRepository;
import com.acme.marsrover.service.AsyncDownloadService;
import com.acme.marsrover.service.DateConversionService;
import com.acme.marsrover.service.FileReaderService;
import com.acme.marsrover.service.ProcessDateService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.rowset.serial.SerialBlob;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ProcessDataServiceImpl extends BaseService implements ProcessDateService {
    @Autowired
    private ImageDetailRepository imageDetailRepository;

    @Autowired
    private AsyncDownloadService asyncDownloadService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DateConversionService dateConversionService;

    @Autowired
    private FileReaderService fileReaderService;

    @Autowired
    @Qualifier("processDateServiceDateParser")
    private DateTimeFormatter dateTimeFormatter;

    @Value("${nasa.api.mars.rover.photos}")
    private String nasaMarsRoverPhotosUrl;

    @Value("${nasa.api.key}")
    private String nasaApiKey;

    @Value("${nasa.api.date.format}")
    private String nasaDateFormat;

    @Value("${get.image.uri}")
    private String getImageUri;

    @Value("${server.servlet.contextPath}")
    private String contextPath;

    @Value("${domain.url}")
    private String domainUrl;

    @Value("${server.port}")
    private String port;

    /**
     * Deletes all entries from the Image table
     *
     */
    @Override
    public void clearDatabase() {
        deleteAllFromImageTable();
    }

    /**
     * Gets date text file scanner, loops through each item and calls the
     *  asynchronous processDate() method
     *
     */
    @Override
    public void process() {
        logger.info("Entering process()");
        //Create scanner for dates text file
        //Scanner scanner = fileReaderService.getScannerForDatesTextFile();
        List<String> dates = fileReaderService.getDatesFromTextFile();

        //Process each date (get images from Nasa API, download images,
        //save into database, and add to map
        for(String date : dates) {
            processDate(date);
        }
        logger.info("Exiting process()");
    }

    /**
     * Asynchronous
     * Processes a date by converting the String date to DateTime Object,
     * calling the NASA API to retrieve list of mars rover photo details for
     * given date, and waits for all images to process then saves into database
     *
     * @param sDate
     * @return
     */
    private CompletableFuture<Void> processDate(String sDate) {

        //Convert string date to datetime object
        DateTime dateTime = dateConversionService.convertDateStringToDateTime(sDate);

        //Check if datetime is null
        if(dateTime != null) {
            //Query database to see if images already exist for date
            List<String> imageUrls = getImageUrlsByDate(dateTime.toDate());

            //Process date
            if(imageUrls == null || imageUrls.isEmpty()) {
                callApiForMarsRoverPhotos(dateTime.toString(nasaDateFormat))
                        .thenApplyAsync(nasaApiPhotoDetails -> {
                            processImages(nasaApiPhotoDetails, dateTime)
                                    .thenApplyAsync(imageEntities -> {
                                        imageDetailRepository.saveAll(imageEntities);
                                        return null;
                                    });
                            return null;
                });
            }
        }

        return CompletableFuture.completedFuture(null);
    }

    /**
     * Asynchronous
     * Processes a list of images by calling the processImage() method and
     * adds them to a list to be returned
     *
     * @param nasaPhotoDetails
     * @param dateTime
     * @return
     */
    private CompletableFuture<List<ImageDetailEntity>> processImages(List<ImageDetailEntity> nasaPhotoDetails,
                                                                     DateTime dateTime) {
        //Create list of completeable futures
        List<CompletableFuture<ImageDetailEntity>> completableFutureList;
        completableFutureList = nasaPhotoDetails.stream()
                .map(imageDetail -> processImage(imageDetail, dateTime))
                .collect(Collectors.toList());


        //Create CompleteableFuture with list of entities
        CompletableFuture<Void> allFutures = CompletableFuture
                .allOf(completableFutureList.toArray(new CompletableFuture[completableFutureList.size()]));

        CompletableFuture<List<ImageDetailEntity>> allCompleteableFuture =
                allFutures.thenApply(future -> completableFutureList.stream()
                           .map(completeFutures -> completeFutures.join())
                           .collect(Collectors.toList())
                );

        return allCompleteableFuture;
    }

    /**
     * Asynchronous
     * Processes a single image by downloading the image, creating url for image,
     * and then adding it to a String list of image urls
     *
     * @param imageDetail
     * @param dateTime
     * @return
     */
    private CompletableFuture<ImageDetailEntity> processImage(ImageDetailEntity imageDetail,
                                                              DateTime dateTime) {
        CompletableFuture<ImageDetailEntity> fImageEntity;
        try {
                fImageEntity = asyncDownloadService.download(imageDetail.getNasaUrl())
                    .thenApplyAsync(imageBytes -> {
                        try {
                            SerialBlob imageBlob = new SerialBlob(imageBytes);
                            imageDetail.setImageBlob(imageBlob);
                            String url = createImageUrl(imageDetail.getId());
                            imageDetail.setUrl(url);
                            imageDetail.setSize(imageBytes.length);
                            CameraEntity cameraEntity = imageDetail.getCamera();
                            if(imageDetail.getRover() != null
                                    && cameraEntity.getIntRoverId() != null
                                    && imageDetail.getRover().getId() == cameraEntity.getIntRoverId()) {
                                cameraEntity.setRover(imageDetail.getRover());
                            }
                            imageDetail.setDate(dateTime.toDate());
                        } catch (Exception e) {
                            logger.error("Error converting bytes to blob");
                            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                    "Error converting bytes to blob", e);
                        }
                return imageDetail;
            });
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error while downloading image for " + dateTime.toString(), e);
        }
        return fImageEntity;
    }

    /**
     * Creates a String url for given photo id
     *
     * @param photoId
     * @return
     */
    private String createImageUrl(int photoId) {
        logger.info("Creating image url for " + photoId);
        String url = domainUrl + ":" + port + contextPath + getImageUri + "/" + photoId;
        logger.info("URL created " + url);
        return url;
    }

    /**
     * Asynchronous
     * Calls NASA API to get photo details for the mars rover given a String date
     *
     * @param sDate
     * @return
     */
    private CompletableFuture<List<ImageDetailEntity>> callApiForMarsRoverPhotos(String sDate) {
        String url = nasaMarsRoverPhotosUrl + "?earth_date=" + sDate + "&api_key=" + nasaApiKey;
        logger.info("Calling API to retrieve photo details for date " + sDate);
        NasaApiPhotosResponse response = restTemplate.getForObject(url, NasaApiPhotosResponse.class);
        logger.info("Photo details retrieved successfully");
        return CompletableFuture.completedFuture(response.getPhotos());
    }

    /**
     * Query to retrieve image urls by date
     *
     * @param date
     * @return
     */
    private List<String> getImageUrlsByDate(Date date) {
        logger.info("Querying for image urls for the date of " + date.toString());
        List<String> imageUrls = imageDetailRepository.getImageUrlByDate(date);
        logger.info("Query found " + imageUrls.size() + " results");
        return imageUrls;
    }

    /**
     * Deletes all entries from the Image table
     *
     */
    private void deleteAllFromImageTable() {
        logger.info("Deleting all from Image table");
        imageDetailRepository.deleteAllFromImageTable();
        logger.info("Delete all complete");
    }

    /**
     * Saves list of entities to database
     *
     * @param imageDetailEntities
     */
    private void saveAll(List<ImageDetailEntity> imageDetailEntities) {
        try {
            logger.info("Saving entity list of size " + imageDetailEntities.size());
            imageDetailRepository.saveAll(imageDetailEntities);
            logger.info("Save successful");
        } catch (Exception e) {
            logger.info("Error saving to database", e);
        }
    }
}
