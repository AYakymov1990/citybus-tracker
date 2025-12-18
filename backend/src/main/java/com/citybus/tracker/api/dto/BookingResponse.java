package com.citybus.tracker.api.dto;

import java.time.LocalDateTime;

public record BookingResponse(Long id, Long routeId, String passengerName, Integer seats, LocalDateTime createdAt) {
}
