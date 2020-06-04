package com.acme.marsrover.dto.response;

import com.acme.marsrover.dto.NasaPhotoDetail;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class NasaApiPhotosResponse {
    @JsonProperty("photos")
    private List<NasaPhotoDetail> photos;
}
