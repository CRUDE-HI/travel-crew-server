package com.crude.travelcrew.domain.email.repository;

import com.crude.travelcrew.domain.email.model.EmailAuthCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailAuthCodeRepository extends CrudRepository<EmailAuthCode, String> {
}
