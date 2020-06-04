package com.acme.marsrover.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class NasaRover {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("landing_date")
    private Date landingDate;

    @JsonProperty("launch_date")
    private Date launchDate;

    @JsonProperty("status")
    private String status;

    @JsonProperty("max_sol")
    private Integer maxSol;

    @JsonProperty("max_date")
    private Date maxDate;

    @JsonProperty("total_photos")
    private Integer totalPhotos;

    @JsonProperty("cameras")
    private List<NasaCamera> cameras;
}
