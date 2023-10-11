package com.crude.travelcrew.domain.crew.service;

import org.springframework.stereotype.Service;

import com.crude.travelcrew.domain.crew.model.dto.ChatMessageReq;
import com.crude.travelcrew.domain.crew.repository.CrewRepository;
import com.crude.travelcrew.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrewMessageServiceImpl implements CrewMessageService {

	private final static String CHAT_EXCHANGE_NAME = "chat.exchange";

	private final CrewRepository crewRepository;
	private final MemberRepository memberRepository;

	@Override
	public void enter(ChatMessageReq request) {

	}

	@Override
	public void send(ChatMessageReq req) {

	}
}
