package kiedam.chatapp.backend.repository;

import kiedam.chatapp.backend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE m.chatRoom.id = ?1")
    List<Message> findAllByChatRoom_Id(Long chatRoomId);
}
