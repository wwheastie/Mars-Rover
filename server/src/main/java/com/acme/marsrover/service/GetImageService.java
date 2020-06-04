package com.acme.marsrover.service;

import com.acme.marsrover.dto.response.GetImageMapResponse;

public interface GetImageService {
    GetImageMapResponse getDateImageUrlMap();
    byte[] getImageBytes(int id);
}
