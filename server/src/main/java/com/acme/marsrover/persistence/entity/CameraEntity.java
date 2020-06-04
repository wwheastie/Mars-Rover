package com.acme.marsrover.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "camera")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CameraEntity {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @JsonProperty("id")
    private Integer id;

    @Column(name = "name", unique = true, nullable = false)
    @JsonProperty("name")
    private String name;

    @Transient
    @JsonProperty("rover_id")
    private Integer intRoverId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "roverid")
    private RoverEntity rover;

    @Column(name = "fullname", unique = true, nullable = false)
    @JsonProperty("full_name")
    private String fullName;
}
