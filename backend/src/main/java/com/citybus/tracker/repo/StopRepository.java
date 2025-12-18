package com.citybus.tracker.repo;

import com.citybus.tracker.domain.Stop;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StopRepository extends JpaRepository<Stop, Long> {

	List<Stop> findByRouteIdOrderBySeq(Long routeId);
}
