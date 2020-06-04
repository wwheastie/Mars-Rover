package com.acme.marsrover.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class ProcessDateResponse {
    private Map<Date, List<String>> dateImageUrlMap;
}
