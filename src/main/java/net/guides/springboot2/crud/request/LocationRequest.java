package net.guides.springboot2.crud.request;

import lombok.Data;

@Data
public class LocationRequest {
    private double userLat;
    private double userLng;
}
