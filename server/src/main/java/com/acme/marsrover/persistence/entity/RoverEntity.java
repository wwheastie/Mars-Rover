package com.acme.marsrover.persistence.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "rover")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoverEntity {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @JsonProperty("id")
    private Integer id;

    @Column(name = "name", unique = true, nullable = false)
    @JsonProperty("name")
    private String name;

    @Column(name = "landing_date")
    @JsonProperty("landing_date")
    private Date landingDate;

    @Column(name = "launch_date")
    @JsonProperty("launch_date")
    private Date launchDate;

    @Column(name = "status")
    @JsonProperty("status")
    private String status;

    @Column(name = "max_sol")
    @JsonProperty("max_sol")
    private Integer maxSol;

    @Column(name = "max_date")
    @JsonProperty("max_date")
    private Date maxDate;

    @Column(name = "total_photos")
    @JsonProperty("total_photos")
    private Integer totalPhotos;

    @OneToMany(mappedBy = "rover")
    @JsonProperty("cameras")
    private List<CameraEntity> cameras;
}
