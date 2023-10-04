package com.crude.travelcrew.domain.board.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crude.travelcrew.domain.board.dto.MemberListReq;
import com.crude.travelcrew.domain.board.entity.Posts;
import com.crude.travelcrew.domain.board.repository.PostsRepository;
import com.crude.travelcrew.domain.member.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;

@Service
public class MemberListService {
	@Autowired
	private PostsRepository postsRepository;
	@Autowired
	private MemberRepository memberRepository;

	// 참가자 추가
	public void addMemberList(Long postId, MemberListReq memberListReq) {
		Posts post = postsRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("게시물을 찾을 수 없음"));
		Member member = memberRepository.findById(memberListReq.getMemberId()).orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없음"));

		post.addMemberList(member);

		postsRepository.save(post);
	}

	// 멤버 리스트 제거
	public void removeMemberList(Long postId, Long memberId) {
		Posts post = postsRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("게시물을 찾을 수 없음"));
		Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없음"));

		post.removeMemberList(member);

		postsRepository.save(post);
	}
}
