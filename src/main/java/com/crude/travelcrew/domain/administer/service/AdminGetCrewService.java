package com.crude.travelcrew.domain.administer.service;

import static com.crude.travelcrew.global.error.type.CrewErrorCode.*;

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
import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.model.entity.CrewComment;
import com.crude.travelcrew.domain.crew.repository.CrewCommentRepository;
import com.crude.travelcrew.domain.crew.repository.CrewRepository;
import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.global.error.exception.CrewException;

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
			.orElseThrow(() -> new CrewException(CREW_NOT_FOUND));

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
			.orElseThrow(() -> new CrewException(CREW_NOT_FOUND));

		crew.blockContent();

		crewRepository.save(crew);
	}

	@Transactional
	public void blockComment(long commentId) {
		CrewComment crewComment = crewCommentRepository.findById(commentId)
			.orElseThrow(() -> new CrewException(COMMENT_NOT_FOUND));

		crewComment.blockContent();

		crewCommentRepository.save(crewComment);
	}
}
