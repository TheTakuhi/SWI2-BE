package kiedam.chatapp.backend.repository;

import kiedam.chatapp.backend.model.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<Chatroom, Long> {
    @Query("SELECT c FROM Chatroom c LEFT JOIN FETCH c.users WHERE c.id = ?1")
    Optional<Chatroom> findChatRoomById(Long chatRoomId);
}
