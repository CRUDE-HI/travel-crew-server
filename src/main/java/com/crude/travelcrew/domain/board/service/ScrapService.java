package com.crude.travelcrew.domain.board.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crude.travelcrew.domain.board.entity.Posts;
import com.crude.travelcrew.domain.board.entity.Scraps;
import com.crude.travelcrew.domain.board.repository.PostsRepository;
import com.crude.travelcrew.domain.board.repository.ScrapRepository;
import com.crude.travelcrew.domain.member.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrapService {
	private final ScrapRepository scrapRepository;
	private final MemberRepository memberRepository;
	private final PostsRepository postsRepository;

	@Transactional
	public void scrapPost(Long crewId, String email) {

		Member member = memberRepository.findByEmail(email);

		if (Objects.isNull(member)){
			throw new IllegalArgumentException("사용자를 찾을수 없습니다.");
		}

		Posts posts = postsRepository.findById(crewId)
			.orElseThrow(() -> new IllegalArgumentException("게시물을 찾을수 없습니다."));

		if (scrapRepository.existsByMemberAndPosts(member,posts)){
			throw new IllegalArgumentException("이미 스크랩하셨습니다.");
		}

		Scraps scraps = Scraps.builder()
			.member(member)
			.posts(posts)
			.build();

		scrapRepository.save(scraps);
	}

	@Transactional
	public void deleteScrapPost(Long crewId, String email) {

		Member member = memberRepository.findByEmail(email);

		if (Objects.isNull(member)){
			throw new IllegalArgumentException("사용자를 찾을수 없습니다.");
		}

		Posts posts = postsRepository.findById(crewId)
			.orElseThrow(() -> new IllegalArgumentException("게시물을 찾을수 없습니다."));

		Scraps scraps = scrapRepository.findByMemberAndPosts(member, posts)
			.orElseThrow(() -> new IllegalArgumentException("스크랩 되어있지 않습니다."));

		scrapRepository.delete(scraps);
	}
}
