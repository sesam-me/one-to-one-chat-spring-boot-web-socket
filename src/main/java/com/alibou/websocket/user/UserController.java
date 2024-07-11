package com.alibou.websocket.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @MessageMapping("/user.addUser") // 사용자가 들어오면 모든 사용자에게 알리고 메시지를 보내야한다.
    @SendTo("/user/public") // 특정 주제로 연결된 모든 사용자에게 새 사용자가 있음을 알린다.
    public User addUser(
            @Payload User user // 알림을 @Payload 여기에 담아 보낸다.
            // @Payload : 클라이언트가 서버로 보낸 실제 메시지를 자동으로 Java객체로 변환하여 메서드에 전달
    ) {
        userService.saveUser(user);
        return user;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public User disconnectUser(
            @Payload User user
    ) {
        userService.disconnect(user);
        return user;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findConnectedUsers() {
        return ResponseEntity.ok(userService.findConnectedUsers());
    }
}
