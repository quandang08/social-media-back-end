package com.socialmedia.backend.repository;

import com.socialmedia.backend.entities.Aura;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AuraChatMessageRepository extends JpaRepository<Aura,Long> {
}
