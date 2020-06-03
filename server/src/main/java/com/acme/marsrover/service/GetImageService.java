package com.acme.marsrover.service;

import com.acme.marsrover.dto.GetImageMapResponse;

public interface GetImageService {
    GetImageMapResponse getDateImageUrlMap();
    byte[] getImageBytes(int id);
}
