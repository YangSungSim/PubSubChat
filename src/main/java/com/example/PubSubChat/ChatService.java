package com.example.PubSubChat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class ChatService implements MessageListener {

    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Autowired
    RedisTemplate<String, String> redisTemplate;


    public void enterChatRoom(String chatRoomName) {
        redisMessageListenerContainer.addMessageListener(this, new ChannelTopic(chatRoomName));

        //사용자 입력을 기다림
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String line = in.nextLine();

            if(line.equals("q")) {
                System.out.println("Quit...");
                break;

            }

            redisTemplate.convertAndSend(chatRoomName, line);
        }

        redisMessageListenerContainer.removeMessageListener(this);

    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        //redis subscrip를 통해서 도착한 메세지를 확인할 수 있음
        System.out.println("Mesage: "+message.toString());
    }
}
