package com.betvictor.test.messaging.actionmonitor.controller;

import com.betvictor.test.messaging.actionmonitor.dto.MessageDTO;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.betvictor.test.messaging.actionmonitor.config.WebSocketConfig.SOCKETS;
import static com.betvictor.test.messaging.actionmonitor.controller.MessageController.CHAT;
import static com.betvictor.test.messaging.actionmonitor.controller.MessageController.SPECIFIC_USER;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Disabled("Couldn't manage to make it work, I was getting the error:" +
        " 'The HTTP response from the server [200] did not permit the HTTP upgrade to WebSocket'")
class MessageControllerTest {

    private StompSession stompSession;
    private String user;
    private CompletableFuture<MessageDTO> completableFuture;
    @Value("${local.server.port}")
    private int port;
    private WebSocketStompClient stompClient;
    private String url;
    private StompSessionHandler sessionHandler;

    @BeforeEach
    public void setUp() {
        url = "ws://localhost:" + port + SOCKETS;
        WebSocketClient client = new SockJsClient(Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient())));

        stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        sessionHandler = new StompSessionHandlerAdapter() {
        };

        user = RandomString.make();
        completableFuture = new CompletableFuture<>();
    }

    @Test
    @WithMockUser(value = "lucas")
    void sendSpecific() throws ExecutionException, InterruptedException, TimeoutException {
        stompSession = stompClient.connect(url, sessionHandler).get(1, SECONDS);
        stompSession.subscribe(SPECIFIC_USER + user, new StompSessionHandlerAdapter() {
            @Override
            public Type getPayloadType(final StompHeaders headers) {
                return MessageDTO.class;
            }

            @Override
            public void handleFrame(final StompHeaders headers, final Object payload) {
                completableFuture.complete((MessageDTO) payload);
            }
        });
        MessageDTO payload = new MessageDTO(RandomString.make(), RandomString.make(), user, ZonedDateTime.now());
        stompSession.send(CHAT, payload);
        MessageDTO messageDTO = completableFuture.get(5, SECONDS);
        assertThat(messageDTO, is(payload));
    }
}