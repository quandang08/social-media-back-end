package com.socialmedia.backend.repository;

import com.socialmedia.backend.entities.Twit;
import com.socialmedia.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TwitRepository extends JpaRepository<Twit, Long> {

    // Lấy tất cả các Twit gốc, sắp xếp theo thời gian tạo mới nhất
    List<Twit> findAllByIsTwitTrueOrderByCreatedAtDesc();

    // Lấy tất cả Twit của user hoặc Twit mà user đã retwit, sắp xếp theo thời gian tạo mới nhất
    List<Twit> findByRetwitUserContainingOrUserIdAndIsTwitTrueOrderByCreatedAtDesc(User user, Long userId);

    // Lấy danh sách Twit mà user đã like, sắp xếp theo thời gian tạo mới nhất
    @Query("SELECT t FROM Twit t JOIN t.likes l WHERE l.user.id = :userId ORDER BY t.createdAt DESC")
    List<Twit> findByLikesUserId(@Param("userId") Long userId);
}
