package com.citybus.tracker.api.dto;

public record CreateStopRequest(String name, Double lat, Double lon, Integer seq) {
}
