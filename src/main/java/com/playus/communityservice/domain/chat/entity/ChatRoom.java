package com.playus.communityservice.domain.chat.entity;


import com.playus.communityservice.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_room")
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="chat_room_id")
    private Long id;

    @Column(nullable = false, length = 255)
    private String roomName;

    @Builder
    private ChatRoom(Long id, String roomName) {
        this.id = id;
        this.roomName = roomName;
    }

    public static ChatRoom create(String roomName) {
        return new ChatRoom(null, roomName);
    }

    public void update(String roomName) {
        this.roomName = roomName;
    }
}
