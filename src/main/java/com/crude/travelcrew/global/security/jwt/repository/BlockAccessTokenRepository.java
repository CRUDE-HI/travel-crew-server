package com.crude.travelcrew.global.security.jwt.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.crude.travelcrew.global.security.jwt.model.BlockAccessToken;

@Repository
public interface BlockAccessTokenRepository extends CrudRepository<BlockAccessToken, String> {
}
