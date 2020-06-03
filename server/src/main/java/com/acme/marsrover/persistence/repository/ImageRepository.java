package com.acme.marsrover.persistence.repository;

import com.acme.marsrover.persistence.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
    @Query("select i.url from ImageEntity i where i.date = :date")
    List<String> getImageUrlByDate(@Param("date") Date date);

    @Query("select i.imageblob from ImageEntity i where i.photoId = :photoId")
    byte[] getBlobByPhotoId(@Param("photoId") int photoId);

    @Transactional
    @Modifying
    @Query("delete from ImageEntity i where i.photoId > 0")
    void deleteAllFromImageTable();
}
