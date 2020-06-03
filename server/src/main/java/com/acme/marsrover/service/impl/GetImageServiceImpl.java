package com.acme.marsrover.service.impl;

import com.acme.marsrover.dto.GetImageMapResponse;
import com.acme.marsrover.persistence.repository.ImageRepository;
import com.acme.marsrover.service.DateConversionService;
import com.acme.marsrover.service.FileReaderService;
import com.acme.marsrover.service.GetImageService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetImageServiceImpl extends BaseService implements GetImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private FileReaderService fileReaderService;

    @Autowired
    private DateConversionService dateConversionService;

    /**
     * Gets scanner for dates file, loops through each,
     * queries the Image table for given date, and adds
     * the date (key) / image urls (value) key-pair to map
     *
     * @return
     */
    @Override
    public GetImageMapResponse getDateImageUrlMap() {
        logger.info("Entering getDateImageUrlMap()");

        //Get scanner for dates text file
        Scanner scanner = fileReaderService.getScannerForDatesTextFile();

        //Create response object
        GetImageMapResponse response = new GetImageMapResponse();
        response.setDateImageUrlMap(new HashMap<>());

        //Loop through dates text file
        while (scanner.hasNextLine()) {
            //Convert date string to DateTime object
            DateTime dateTime = dateConversionService.convertDateStringToDateTime(scanner.nextLine());

            //Query database with date if date is not null
            if(dateTime != null) {
                List<String> imageUrls = getImageUrlsByDate(dateTime.toDate());
                if(imageUrls != null && !imageUrls.isEmpty()) {
                    response.getDateImageUrlMap().put(dateTime.toDate(), imageUrls);
                }
            }
        }

        logger.info("Exiting getDateImageUrlMap()");
        return response;
    }

    /**
     * Gets image bytes from Image table given photo id
     *
     * @param id
     * @return
     */
    @Override
    public byte[] getImageBytes(int id) {
        return getImageBlobByPhotoId(id);
    }

    /**
     * Get image urls from Image table given date
     *
     * @param date
     * @return
     */
    private List<String> getImageUrlsByDate(Date date) {
        logger.info("Querying for image urls for the date of " + date.toString());
        List<String> imageUrls = imageRepository.getImageUrlByDate(date);
        logger.info("Query found " + imageUrls.size() + " results");
        return imageUrls;
    }

    /**
     * Gets image bytes from Image table given photo id
     *
     * @param photoId
     * @return
     */
    private byte[] getImageBlobByPhotoId(int photoId) {
        logger.info("Quering for image blob with photoId of " + photoId);
        byte[] imageBlob = imageRepository.getBlobByPhotoId(photoId);
        logger.info("Query was successful");
        return imageBlob;
    }
}
