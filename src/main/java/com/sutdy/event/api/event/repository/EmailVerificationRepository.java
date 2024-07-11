package com.sutdy.event.api.event.repository;

import com.sutdy.event.api.event.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, String> {
}
