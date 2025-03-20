package com.socialmedia.backend.repository;

import com.socialmedia.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u")
    List<User> getAllUsers();

    Optional<User> findByEmail(String email);
    @Query("SELECT DISTINCT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%'))")

    List<User> searchUser(@Param("query") String query);

    @Query("select u from User u where u.email = :email ")
    User findUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id <> :userId AND u.id NOT IN " +
            "(SELECT f.id FROM User f JOIN f.followers followers WHERE followers.id = :userId)")
    List<User> findUsersNotFollowedBy(@Param("userId") Long userId);
}
