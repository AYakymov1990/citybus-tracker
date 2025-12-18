package com.citybus.tracker.api;

import com.citybus.tracker.api.dto.BookingResponse;
import com.citybus.tracker.api.dto.CreateBookingRequest;
import com.citybus.tracker.domain.Booking;
import com.citybus.tracker.service.BookingService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

	private final BookingService bookingService;

	public BookingController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	@PostMapping
	public ResponseEntity<BookingResponse> createBooking(@RequestBody CreateBookingRequest request) {
		Booking booking = bookingService.createBooking(request.routeId(), request.passengerName(), request.seats());
		return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(booking));
	}

	@GetMapping
	public List<BookingResponse> getBookings(@RequestParam Long routeId) {
		return bookingService.getBookingsByRoute(routeId).stream()
			.map(this::toResponse)
			.collect(Collectors.toList());
	}

	private BookingResponse toResponse(Booking booking) {
		return new BookingResponse(
			booking.getId(),
			booking.getRoute().getId(),
			booking.getPassengerName(),
			booking.getSeats(),
			booking.getCreatedAt()
		);
	}
}
