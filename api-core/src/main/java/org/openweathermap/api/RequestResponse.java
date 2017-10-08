package org.openweathermap.api;

import lombok.Data;

import java.util.Date;

@Data
public class RequestResponse {
    private Date time;
    private String query;
    private String response;
}
