package com.citybus.tracker.api;

import com.citybus.tracker.api.dto.CreateRouteRequest;
import com.citybus.tracker.api.dto.CreateStopRequest;
import com.citybus.tracker.api.dto.RouteResponse;
import com.citybus.tracker.api.dto.StopResponse;
import com.citybus.tracker.domain.Route;
import com.citybus.tracker.domain.Stop;
import com.citybus.tracker.service.RouteService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

	private final RouteService routeService;

	public RouteController(RouteService routeService) {
		this.routeService = routeService;
	}

	@GetMapping
	public List<RouteResponse> getRoutes() {
		return routeService.getRoutes().stream()
			.map(this::toRouteResponse)
			.collect(Collectors.toList());
	}

	@PostMapping
	public ResponseEntity<RouteResponse> createRoute(@RequestBody CreateRouteRequest request) {
		Route route = routeService.createRoute(request.name(), request.city());
		return ResponseEntity.status(HttpStatus.CREATED).body(toRouteResponse(route));
	}

	@GetMapping("/{id}")
	public RouteResponse getRoute(@PathVariable Long id) {
		return toRouteResponse(routeService.getRoute(id));
	}

	@PostMapping("/{id}/stops")
	public ResponseEntity<StopResponse> addStop(@PathVariable Long id, @RequestBody CreateStopRequest request) {
		Stop stop = routeService.addStop(id, request.name(), request.lat(), request.lon(), request.seq());
		return ResponseEntity.status(HttpStatus.CREATED).body(toStopResponse(stop));
	}

	@GetMapping("/{id}/stops")
	public List<StopResponse> getStops(@PathVariable Long id) {
		return routeService.getStops(id).stream()
			.map(this::toStopResponse)
			.collect(Collectors.toList());
	}

	private RouteResponse toRouteResponse(Route route) {
		return new RouteResponse(route.getId(), route.getName(), route.getCity(), route.getActive());
	}

	private StopResponse toStopResponse(Stop stop) {
		return new StopResponse(stop.getId(), stop.getName(), stop.getLat(), stop.getLon(), stop.getSeq());
	}
}
