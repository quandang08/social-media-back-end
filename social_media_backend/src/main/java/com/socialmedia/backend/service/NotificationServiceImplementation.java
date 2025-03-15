package com.socialmedia.backend.service;

import com.socialmedia.backend.dto.NotificationDto;
import com.socialmedia.backend.exception.UserException;
import com.socialmedia.backend.model.Notification;
import com.socialmedia.backend.repository.NotificationRepository;
import com.socialmedia.backend.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImplementation implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<NotificationDto> getNotificationsForUser(Long userId) throws UserException {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return notifications.stream()
                .map(NotificationMapper::toNotificationDto)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationDto markAsRead(Long notificationId, Long userId) throws UserException {
        // 1) Tìm notification theo ID
        Notification noti = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new UserException("Notification not found"));

        // 2) Kiểm tra xem thông báo này có thuộc về user hiện tại không
        if (!noti.getUserId().equals(userId)) {
            throw new UserException("Bạn không có quyền cập nhật thông báo này");
        }

        // 3) Đánh dấu thông báo là đã đọc
        noti.setRead(true);
        noti.setUpdatedAt(LocalDateTime.now());
        notificationRepository.save(noti);

        // 4) Trả về DTO của notification đã cập nhật
        return NotificationMapper.toNotificationDto(noti);
    }
}
