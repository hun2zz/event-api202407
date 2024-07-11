package com.sutdy.event.api.event.repository;

import com.sutdy.event.api.event.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventRepositoryCustom {

    Page<Event> findEvents(Pageable pageable , String sort);

    //...


    ///....


    //....
}
