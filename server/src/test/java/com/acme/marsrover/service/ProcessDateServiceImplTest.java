package com.acme.marsrover.service;


import com.acme.marsrover.dto.response.NasaApiPhotosResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ProcessDateServiceImplTest {
    private RestTemplate restTemplate;

    private String nasaMarsRoverPhotoUrl;

    private String nasaApiKey;

    @Before
    public void setUp() {
        restTemplate = new RestTemplate();
        nasaMarsRoverPhotoUrl = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos";
        nasaApiKey = "AGz7LKqvlkjaNQCkiirWRDPkBv8KS2hEVRNJdM3f";
    }

    @After
    public void tearDown() {
        restTemplate = null;
    }

    @Test
    public void testValidNasaAPIResponse() {
        String validUrl = nasaMarsRoverPhotoUrl + "?earth_date=2017-2-27" + "&api_key=" + nasaApiKey;
        NasaApiPhotosResponse response = restTemplate.getForObject(validUrl, NasaApiPhotosResponse.class);
        assertNotNull(response);
    }
}
