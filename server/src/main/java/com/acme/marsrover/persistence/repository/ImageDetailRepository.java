package com.acme.marsrover.persistence.repository;

import com.acme.marsrover.persistence.entity.ImageDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;

import javax.persistence.TemporalType;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface ImageDetailRepository extends JpaRepository<ImageDetailEntity, Integer> {
    @Query("select i.url from ImageDetailEntity i where i.date = :date")
    List<String> getImageUrlByDate(@Param("date") Date date);

    @Query("select i.imageBlob from ImageDetailEntity i where i.id = :id")
    byte[] getBlobByPhotoId(@Param("id") int id);

    @Transactional
    @Modifying
    @Query("delete from ImageDetailEntity i where i.id > 0")
    void deleteAllFromImageTable();
}
