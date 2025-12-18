package com.citybus.tracker.repo;

import com.citybus.tracker.domain.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
