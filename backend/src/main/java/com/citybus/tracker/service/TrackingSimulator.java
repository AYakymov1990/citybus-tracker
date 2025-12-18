package com.citybus.tracker.service;

import com.citybus.tracker.domain.Bus;
import com.citybus.tracker.domain.Stop;
import com.citybus.tracker.repo.BusRepository;
import com.citybus.tracker.repo.RouteRepository;
import com.citybus.tracker.repo.StopRepository;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ConditionalOnProperty(value = "tracker.simulator.enabled", havingValue = "true", matchIfMissing = true)
public class TrackingSimulator {

	private static final Logger log = LoggerFactory.getLogger(TrackingSimulator.class);
	private final Random random = new Random();

	private final RouteRepository routeRepository;
	private final StopRepository stopRepository;
	private final BusRepository busRepository;

	public TrackingSimulator(RouteRepository routeRepository, StopRepository stopRepository, BusRepository busRepository) {
		this.routeRepository = routeRepository;
		this.stopRepository = stopRepository;
		this.busRepository = busRepository;
	}

	@Transactional
	@Scheduled(fixedDelayString = "${tracker.simulator.delay:5000}")
	public void tick() {
		try {
			log.info("SIM tick");
			simulate();
		}
		catch (Exception ex) {
			log.error("SIM error", ex);
		}
	}

	private void simulate() {
		routeRepository.findAll().forEach(route -> {
			Long routeId = route.getId();
			if (routeId == null) {
				return;
			}

			List<Stop> stops = stopRepository.findByRouteIdOrderBySeq(routeId);
			if (stops.size() < 2) {
				return;
			}

			List<Bus> buses = busRepository.findByRouteId(routeId);
			if (buses.isEmpty()) {
				createBus(routeId, stops.get(0));
				return;
			}

			Stop targetStop = stops.get(random.nextInt(stops.size()));
			for (Bus bus : buses) {
				updatePosition(bus, targetStop);
			}
		});
	}

	private void createBus(Long routeId, Stop firstStop) {
		Bus bus = new Bus();
		bus.setRoute(routeRepository.getReferenceById(routeId));
		bus.setCode("BUS-" + routeId);
		bus.setLat(firstStop.getLat());
		bus.setLon(firstStop.getLon());
		busRepository.save(bus);
		log.info("Created bus for route {}", routeId);
	}

	private void updatePosition(Bus bus, Stop targetStop) {
		bus.setLat(jitter(targetStop.getLat()));
		bus.setLon(jitter(targetStop.getLon()));
		busRepository.save(bus);
	}

	private double jitter(Double value) {
		if (value == null) {
			return 0;
		}
		double delta = (random.nextDouble() - 0.5) / 500;
		return value + delta;
	}
}
