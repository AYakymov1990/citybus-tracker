package com.citybus.tracker.api.dto;

public record CreateBookingRequest(Long routeId, String passengerName, Integer seats) {
}
