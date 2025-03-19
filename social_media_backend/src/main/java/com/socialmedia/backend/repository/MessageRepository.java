package com.socialmedia.backend.repository;

import com.socialmedia.backend.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    //CRUD cơ bản
    List<Message> findBySenderIdAndReceiverIdOrderByCreatedAt(Long senderId, Long receiverId);

    //hỗ trợ lấy lịch sử chat 1-1 giữa hai user
    List<Message> findByReceiverIdAndIsReadFalse(Long receiverId);
}