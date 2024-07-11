package com.sutdy.event.api.event.repository;

import com.sutdy.event.api.event.entity.EventUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventUserRepository extends JpaRepository<EventUser, String> {


    // query method로 Jpql 생성
    boolean existsByEmail(String email);
}
