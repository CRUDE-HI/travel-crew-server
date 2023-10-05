package com.crude.travelcrew.domain.board.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crude.travelcrew.domain.board.entity.Posts;
import com.crude.travelcrew.domain.board.entity.Scraps;
import com.crude.travelcrew.domain.member.entity.Member;

public interface ScrapRepository extends JpaRepository<Scraps, Long> {
	Optional<Scraps> findByMemberAndPosts(Member member, Posts posts);

	boolean existsByMemberAndPosts(Member member, Posts posts);
}
