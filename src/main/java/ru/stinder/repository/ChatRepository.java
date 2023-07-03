package ru.stinder.repository;

import ru.stinder.entity.messenger.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat,Long> {
    Chat findByInviteCode(String code);

}
