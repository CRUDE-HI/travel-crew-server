package com.crude.travelcrew.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
	basePackages = {"com.crude.travelcrew.domain.member.repository",
		"com.crude.travelcrew.domain.crew.repository",
		"com.crude.travelcrew.domain.record.repository",
		"com.crude.travelcrew.domain.report.repository",
		"com.crude.travelcrew.domain.notification.repository"
	}
)
public class JpaConfig {
}
