package com.sutdy.event.api.event.repository;

import com.sutdy.event.api.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> , EventRepositoryCustom{
}
