package com.acme.marsrover.dto;

import com.acme.marsrover.model.NasaApiPhotoDetail;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NasaApiPhotosResponse {
    @JsonProperty("photos")
    private List<NasaApiPhotoDetail> photos;
}
