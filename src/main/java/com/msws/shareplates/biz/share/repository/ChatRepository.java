package com.msws.shareplates.biz.share.repository;

import com.msws.shareplates.biz.share.entity.Chat;
import com.msws.shareplates.common.code.ChatTypeCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findFirstByTypeAndShareIdAndUserIdOrderByCreationDateDesc(ChatTypeCode type, long shareId, long userId);
}

