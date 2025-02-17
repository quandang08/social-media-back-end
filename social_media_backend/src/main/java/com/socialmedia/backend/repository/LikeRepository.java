package com.socialmedia.backend.repository;

import com.socialmedia.backend.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query("SELECT l FROM Like l WHERE l.user.id = :userId AND l.twit.id = :twitId")
    Optional<Like> isLikeExists(@Param("userId") Long userId, @Param("twitId") Long twitId);

    @Query("SELECT l FROM Like l WHERE l.twit.id = :twitId")
    List<Like> findByTwitId(@Param("twitId") Long twitId);
}
