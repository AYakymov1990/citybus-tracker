package com.citybus.tracker.repo;

import com.citybus.tracker.domain.Booking;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByRouteIdOrderByCreatedAtDesc(Long routeId);
}
