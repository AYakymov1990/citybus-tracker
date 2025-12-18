package com.citybus.tracker.repo;

import com.citybus.tracker.domain.Bus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRepository extends JpaRepository<Bus, Long> {

	List<Bus> findByRouteId(Long routeId);
}
