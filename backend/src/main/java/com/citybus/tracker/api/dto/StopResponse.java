package com.citybus.tracker.api.dto;

public record StopResponse(Long id, String name, Double lat, Double lon, Integer seq) {
}
