package com.acme.marsrover.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "image")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageEntity {
    @Id
    @Column(name = "photoid", nullable = false)
    private Integer photoId;

    @Column(name = "imageblob", nullable = false)
    private byte[] imageblob;

    @Column(name = "nasaurl", unique = true, nullable = false)
    private String nasaurl;

    @Column(name = "url", unique = true, nullable = false)
    private String url;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "date", nullable = false, columnDefinition = "DATE")
    private Date date;

    @Column(name = "size", nullable = false)
    private Integer size;
}
