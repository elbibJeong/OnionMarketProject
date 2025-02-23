package com.youprice.onion.entity.chat;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Chat{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "chat_id")
    private Long id; //채팅번호(각 채팅 문장의 번호) PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="chatroom_id")
    private Chatroom chatroom; //채팅방번호 FK

    private String message; //메세지
    private String chatImageName; //첨부이미지
    private char readOrNot; //읽음 표시
    private LocalDateTime sendingTime; //전송 시간

}
