package com.citybus.tracker.service;

import com.citybus.tracker.domain.Route;
import com.citybus.tracker.domain.Stop;
import com.citybus.tracker.repo.RouteRepository;
import com.citybus.tracker.repo.StopRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class RouteService {

	private final RouteRepository routeRepository;
	private final StopRepository stopRepository;

	public RouteService(RouteRepository routeRepository, StopRepository stopRepository) {
		this.routeRepository = routeRepository;
		this.stopRepository = stopRepository;
	}

	@Transactional(readOnly = true)
	public List<Route> getRoutes() {
		return routeRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Route getRoute(Long id) {
		return routeRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found"));
	}

	public Route createRoute(String name, String city) {
		Route route = new Route();
		route.setName(name);
		route.setCity(city);
		return routeRepository.save(route);
	}

	public Stop addStop(Long routeId, String name, Double lat, Double lon, Integer seq) {
		Route route = getRoute(routeId);
		Stop stop = new Stop();
		stop.setRoute(route);
		stop.setName(name);
		stop.setLat(lat);
		stop.setLon(lon);
		stop.setSeq(seq);
		return stopRepository.save(stop);
	}

	@Transactional(readOnly = true)
	public List<Stop> getStops(Long routeId) {
		ensureRouteExists(routeId);
		return stopRepository.findByRouteIdOrderBySeq(routeId);
	}

	private void ensureRouteExists(Long routeId) {
		if (!routeRepository.existsById(routeId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found");
		}
	}
}
