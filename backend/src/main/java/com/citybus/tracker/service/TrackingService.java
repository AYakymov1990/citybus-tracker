package com.citybus.tracker.service;

import com.citybus.tracker.domain.Bus;
import com.citybus.tracker.repo.BusRepository;
import com.citybus.tracker.repo.RouteRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class TrackingService {

	private final BusRepository busRepository;
	private final RouteRepository routeRepository;

	public TrackingService(BusRepository busRepository, RouteRepository routeRepository) {
		this.busRepository = busRepository;
		this.routeRepository = routeRepository;
	}

	public List<Bus> getBusPositions(Long routeId) {
		if (routeId == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "routeId is required");
		}
		if (!routeRepository.existsById(routeId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found");
		}
		return busRepository.findByRouteId(routeId);
	}
}
