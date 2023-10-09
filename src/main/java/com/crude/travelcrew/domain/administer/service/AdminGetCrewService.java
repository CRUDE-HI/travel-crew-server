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
import com.crude.travelcrew.domain.crew.model.entity.Crew;
import com.crude.travelcrew.domain.crew.repository.CrewRepository;
import com.crude.travelcrew.domain.member.model.entity.Member;

@Service
public class AdminGetCrewService {

	@Autowired
	CrewRepository crewRepository;

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
}
