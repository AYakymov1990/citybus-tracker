package com.citybus.tracker.api;

import com.citybus.tracker.api.dto.BusPositionResponse;
import com.citybus.tracker.domain.Bus;
import com.citybus.tracker.service.TrackingService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/buses")
public class TrackingController {

	private final TrackingService trackingService;

	public TrackingController(TrackingService trackingService) {
		this.trackingService = trackingService;
	}

	@GetMapping("/positions")
	public List<BusPositionResponse> getBusPositions(@RequestParam Long routeId) {
		return trackingService.getBusPositions(routeId).stream()
			.map(this::toResponse)
			.collect(Collectors.toList());
	}

	private BusPositionResponse toResponse(Bus bus) {
		return new BusPositionResponse(bus.getId(), bus.getCode(), bus.getLat(), bus.getLon(), bus.getUpdatedAt());
	}
}
