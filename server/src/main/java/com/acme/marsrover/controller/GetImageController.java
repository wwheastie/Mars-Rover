package com.acme.marsrover.controller;

import com.acme.marsrover.dto.GetImageMapResponse;
import com.acme.marsrover.service.GetImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;

@RestController
public class GetImageController {
    @Autowired
    GetImageService getImageService;

    @GetMapping("/get/map")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin
    public GetImageMapResponse getDateImageUrlMap() {
        return getImageService.getDateImageUrlMap();
    }

    @GetMapping(value = "/get/image/{id}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin
    public byte[] getImage(@PathVariable("id") int id) {
        return getImageService.getImageBytes(id);
    }
}
