package com.crude.travelcrew.domain.crew.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.crude.travelcrew.domain.crew.model.dto.CrewCommentReq;
import com.crude.travelcrew.domain.crew.model.dto.CrewCommentRes;
import com.crude.travelcrew.domain.crew.model.dto.CrewListRes;
import com.crude.travelcrew.domain.crew.model.dto.CrewReq;
import com.crude.travelcrew.domain.crew.model.dto.CrewRes;
import com.crude.travelcrew.domain.crew.model.entity.CrewMessage;

public interface CrewService {

	CrewRes createCrew(CrewReq requestDto, MultipartFile image, String email);

	CrewRes updateCrew(Long crewId, CrewReq request, String email);

	Map<String, String> deleteCrew(Long crewId, String email);

	CrewRes crewView(Long id);

	void enterCrewChat(Long crewId, String token);

	List<CrewMessage> getChatHistory(Long crewId, int page, int size);

	List<CrewListRes> getCrewList(String keyword, Pageable pageable);

	List<CrewCommentRes> getCommentList(long crewId, Pageable pageable);

	void createComment(long crewId, CrewCommentReq commentReq);

	void modifyComment(long commentId, CrewCommentReq commentReq);

	void deleteComment(long commentId);
}
