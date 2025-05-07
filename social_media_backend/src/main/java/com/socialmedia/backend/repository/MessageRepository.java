package com.socialmedia.backend.repository;

import com.socialmedia.backend.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderIdAndReceiverIdOrderByCreatedAt(Long senderId, Long receiverId);
}