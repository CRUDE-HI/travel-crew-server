package com.crude.travelcrew.domain.administer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crude.travelcrew.domain.administer.dto.getCrew.ADCrewListReq;
import com.crude.travelcrew.domain.administer.dto.getCrew.ADCrewListRes;
import com.crude.travelcrew.domain.administer.dto.getCrew.ADCrewListResponseDto;
import com.crude.travelcrew.domain.administer.dto.getCrew.ADGetCrewRes;
import com.crude.travelcrew.domain.crew.model.dto.CrewCommentReq;
import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.model.entity.CrewComment;
import com.crude.travelcrew.domain.crew.repository.CrewCommentRepository;
import com.crude.travelcrew.domain.crew.repository.CrewRepository;
import com.crude.travelcrew.domain.member.model.entity.Member;

@Service
public class AdminGetCrewService {

	@Autowired
	CrewRepository crewRepository;

	@Autowired
	CrewCommentRepository crewCommentRepository;

	public List<ADCrewListResponseDto> convertToDto(List<Crew> crews) {
		return crews.stream()
			.map(crew -> {
					Member member = crew.getMember();
					return new ADCrewListResponseDto(
						crew.getCrewId(),
						crew.getTitle(),
						member.getNickname(),
						crew.getCreatedAt(),
						crew.getUpdatedAt()
					);
				}
			).collect(Collectors.toList());
	}

	@Transactional
	public ADCrewListRes getList(ADCrewListReq ADCrewListReq) {
		Page<Crew> page = crewRepository.findAll(ADCrewListReq.pageable());

		ADCrewListRes ADCrewListRes = new ADCrewListRes(convertToDto(page.getContent()), (int)page.getTotalElements(),
			ADCrewListReq.getPage());

		return ADCrewListRes;
	}

	@Transactional
	public ADGetCrewRes getCrew(Long crewId) {
		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new IllegalArgumentException("해당 글을 찾을 수 없습니다."));

		Member member = crew.getMember();

		return new ADGetCrewRes(
			crew.getCrewId(),
			crew.getTitle(),

			member.getNickname(),

			crew.getThumbnailImgUrl(),
			crew.getCrewStatus(),
			crew.getMaxCrew(),
			crew.getTravelStart(),
			crew.getTravelEnd(),
			crew.getLatitude(),
			crew.getLongitude(),
			crew.getCrewContent()
		);
	}

	@Transactional
	public void blockAndDeleteImages(Long crewId) {
		Crew crew = crewRepository.findById(crewId)
			.orElseThrow(() -> new IllegalArgumentException("해당 글을 찾을 수 없습니다."));

		crew.blockContent();

		crewRepository.save(crew);
	}

	public void blockComment(long commentId) {
		CrewComment crewComment = crewCommentRepository.findById(commentId)
			.orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));

		crewComment.blockContent();

		crewCommentRepository.save(crewComment);
	}
}
