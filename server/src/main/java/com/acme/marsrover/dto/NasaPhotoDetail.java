package com.acme.marsrover.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class NasaPhotoDetail {
    @JsonProperty("id")
    private Integer photoId;

    @JsonProperty("sol")
    private Integer sol;

    @JsonProperty("camera")
    private NasaCamera camera;

    @JsonProperty("img_src")
    private String imgSrc;

    @JsonProperty("earth_date")
    private Date earthDate;

    @JsonProperty("rover")
    private NasaRover rover;
}
