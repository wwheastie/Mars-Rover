package com.acme.marsrover.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Blob;
import java.util.Date;

@Entity
@Table(name = "imagedetail")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDetailEntity {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @JsonProperty("id")
    private Integer id;

    @Column(name = "sol")
    @JsonProperty("sol")
    private Integer sol;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "camerid", nullable = false)
    @JsonProperty("camera")
    private CameraEntity camera;

    @Column(name = "nasaurl", unique = true, nullable = false)
    @JsonProperty("img_src")
    private String nasaUrl;

    @Column(name = "earthdate", nullable = false)
    @JsonProperty("earth_date")
    private Date earthDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "roverid", nullable = false)
    @JsonProperty("rover")
    private RoverEntity rover;

    @Column(name = "imageblob", nullable = false)
    private Blob imageBlob;

    @Column(name = "url", unique = true, nullable = false)
    private String url;

    @Column(name = "size", nullable = false)
    private Integer size;

    @Temporal(TemporalType.DATE)
    @Column(name = "date", nullable = false)
    private Date date;
}
