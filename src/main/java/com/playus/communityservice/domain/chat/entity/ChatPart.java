package com.playus.communityservice.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chatpart")
public class ChatPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="chat_part_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id", nullable = false)
    private ChatRoom chatRoom;

    @Builder
    private ChatPart(Long userId, ChatRoom chatRoom) {
        this.userId = userId;
        this.chatRoom = chatRoom;
    }

    public static ChatPart create(Long userId, ChatRoom chatRoom) {
        return ChatPart.builder()
                .userId(userId)
                .chatRoom(chatRoom)
                .build();
    }

    public void update(Long userId, ChatRoom chatRoom) {
        this.userId = userId;
        this.chatRoom = chatRoom;
    }
}

