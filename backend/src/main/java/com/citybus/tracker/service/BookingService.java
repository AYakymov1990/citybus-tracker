package com.citybus.tracker.service;

import com.citybus.tracker.domain.Booking;
import com.citybus.tracker.domain.Route;
import com.citybus.tracker.repo.BookingRepository;
import com.citybus.tracker.repo.RouteRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class BookingService {

	private final BookingRepository bookingRepository;
	private final RouteRepository routeRepository;

	public BookingService(BookingRepository bookingRepository, RouteRepository routeRepository) {
		this.bookingRepository = bookingRepository;
		this.routeRepository = routeRepository;
	}

	public Booking createBooking(Long routeId, String passengerName, Integer seats) {
		Route route = routeRepository.findById(routeId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found"));
		Booking booking = new Booking();
		booking.setRoute(route);
		booking.setPassengerName(passengerName);
		booking.setSeats(seats);
		return bookingRepository.save(booking);
	}

	@Transactional(readOnly = true)
	public List<Booking> getBookingsByRoute(Long routeId) {
		ensureRouteExists(routeId);
		return bookingRepository.findByRouteIdOrderByCreatedAtDesc(routeId);
	}

	private void ensureRouteExists(Long routeId) {
		if (!routeRepository.existsById(routeId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found");
		}
	}
}
