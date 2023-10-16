package com.crude.travelcrew.domain.notification.service;

import static com.crude.travelcrew.global.error.type.MemberErrorCode.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crude.travelcrew.domain.member.model.entity.Member;
import com.crude.travelcrew.domain.member.repository.MemberRepository;
import com.crude.travelcrew.domain.notification.model.dto.Message;
import com.crude.travelcrew.domain.notification.model.dto.NotificationInfoRes;
import com.crude.travelcrew.domain.notification.model.entity.Notification;
import com.crude.travelcrew.domain.notification.repository.NotificationRepository;
import com.crude.travelcrew.global.error.exception.MemberException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;
	private final MemberRepository memberRepository;

	@Override
	@Transactional(readOnly = true)
	public Slice<NotificationInfoRes> getNotificationList(Pageable pageable, Long lastId, String email) {
		return notificationRepository.findByEmail(pageable, lastId, email);
	}

	@Override
	@Transactional
	public Map<String, String> remove(Long notificationId, String email) {

		Notification notification = notificationRepository.findByEmailAndId(email, notificationId)
			.orElseThrow(() -> new RuntimeException("알림을 삭제할 수 없습니다."));

		notificationRepository.delete(notification);
		return getMessage("알림이 삭제되었습니다.");
	}

	@Override
	@Transactional
	public Map<String, String> read(Long notificationId, String email) {

		Notification notification = notificationRepository.findByEmailAndId(email, notificationId)
			.orElseThrow(() -> new RuntimeException("확인 가능한 알림이 없습니다."));

		if(notification.isRead()) {
			return getMessage("이미 확인한 알림입니다.");
		}

		notification.read();
		return getMessage("알림을 확인하였습니다.");
	}

	@Override
	public void send(Message message) {
		String receiver = message.getNickname();

		Member member = memberRepository.findByNickname(receiver)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		Notification notification = Notification.builder()
			.notificationType(message.getType())
			.content(receiver + message.getType().getText())
			.isRead(false)
			.relatedUrl(String.format("/crew/%d", message.getTargetId()))
			.member(member)
			.build();

		notificationRepository.save(notification);
	}

	private static Map<String, String> getMessage(String message) {
		Map<String, String> result = new HashMap<>();
		result.put("result", message);
		return result;
	}
}
