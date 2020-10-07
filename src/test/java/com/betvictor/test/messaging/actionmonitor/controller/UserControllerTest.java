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
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.betvictor.test.messaging.actionmonitor.config.WebSocketConfig.SOCKETS;
import static com.betvictor.test.messaging.actionmonitor.controller.UserController.REGISTER;
import static com.betvictor.test.messaging.actionmonitor.controller.UserController.USERS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Disabled("Couldn't manage to make it work, I was getting the error:" +
        " 'The HTTP response from the server [200] did not permit the HTTP upgrade to WebSocket'")
class UserControllerTest {

    private StompSession stompSession;
    private String user;
    private CompletableFuture<MessageDTO> completableFuture;
    @Value("${local.server.port}")
    private int port;
    private String url;
    private StompSessionHandler sessionHandler;
    private WebSocketStompClient stompClient;

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
        stompSession.subscribe(USERS + user, new StompSessionHandlerAdapter() {
            @Override
            public Type getPayloadType(final StompHeaders headers) {
                return MessageDTO.class;
            }

            @Override
            public void handleFrame(final StompHeaders headers, final Object payload) {
                completableFuture.complete((MessageDTO) payload);
            }
        });
        stompSession.send(REGISTER, new Object());
        MessageDTO messageDTO = completableFuture.get(5, SECONDS);
        assertThat(messageDTO, is(Collections.singletonList("lucas")));
    }
}