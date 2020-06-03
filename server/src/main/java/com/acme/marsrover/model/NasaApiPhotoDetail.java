package com.acme.marsrover.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class NasaApiPhotoDetail {
    @JsonProperty("id")
    private Integer photoId;
    @JsonProperty("img_src")
    private String imgSrc;
}
