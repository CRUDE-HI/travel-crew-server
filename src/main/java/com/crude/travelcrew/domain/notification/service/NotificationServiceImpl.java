package com.crude.travelcrew.domain.notification.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crude.travelcrew.domain.notification.model.dto.NotificationInfoRes;
import com.crude.travelcrew.domain.notification.model.entity.Notification;
import com.crude.travelcrew.domain.notification.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;

	@Override
	@Transactional(readOnly = true)
	public Slice<NotificationInfoRes> getNotificationList(Pageable pageable, Long lastId, String email) {
		return notificationRepository.findByEmail(pageable, lastId, email);
	}

	@Override
	@Transactional
	public Map<String, String> remove(Long notificationId, String email) {

		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new RuntimeException("알림이 존재하지 않습니다."));

		if(!Objects.equals(notification.getMember().getEmail(), email)) {
			throw new RuntimeException("알림 삭제 권한이 없습니다.");
		}

		notificationRepository.deleteById(notificationId);
		return getMessage("알림이 삭제되었습니다.");
	}

	private static Map<String, String> getMessage(String message) {
		Map<String, String> result = new HashMap<>();
		result.put("result", message);
		return result;
	}
}
