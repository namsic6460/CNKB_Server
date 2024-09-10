package lkd.namsic.cnkb.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;

    private WebSocketSession client = null;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.info("Received - {}", message.getPayload());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        if (this.client != null && this.client.isOpen()) {
            this.client.close();
        }

        this.client = session;
        log.info("Connected");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

        this.client = null;
        log.info("Disconnected");
    }

    public <T> void sendMessage(T payload) {
        if (this.client == null || !this.client.isOpen()) {
            return;
        }

        try {
            String body = this.objectMapper.writeValueAsString(payload);
            this.client.sendMessage(new TextMessage(body));

            log.info("Sent Message - {}", body);
        } catch (Exception e) {
            log.error("Failed to send message", e);
        }
    }
}
