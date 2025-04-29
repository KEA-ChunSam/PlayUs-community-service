package com.playus.communityservice.domain.chat.entity;

import com.playus.communityservice.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_messages")
public class ChatMessage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="chat_message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatpart_id", nullable = false)
    private ChatPart chatPart;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false, name = "is_read")
    private Boolean isRead;

    @Builder
    private ChatMessage(ChatPart chatPart, String message, Boolean isRead) {
        this.chatPart = chatPart;
        this.message = message;
        this.isRead = isRead;
    }

    public static ChatMessage create(ChatPart chatPart, String message) {
        return ChatMessage.builder()
                .chatPart(chatPart)
                .message(message)
                .isRead(false)
                .build();
    }

    public void updateMessage(ChatMessage chatMessage) {
        this.message = chatMessage.getMessage();
    }

    public void updateIsread(ChatMessage chatMessage) {
        this.isRead = chatMessage.getIsRead();
    }


}
