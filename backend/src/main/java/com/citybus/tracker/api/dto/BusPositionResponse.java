package com.citybus.tracker.api.dto;

import java.time.LocalDateTime;

public record BusPositionResponse(Long id, String code, Double lat, Double lon, LocalDateTime updatedAt) {
}
