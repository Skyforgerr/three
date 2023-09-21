package com.example.three;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Flux;

public class RSocketClient {
    public static void main(String[] args) {
        RSocket rSocket = RSocketFactory.connect()
                .transport(TcpClientTransport.create("localhost", 7000))
                .start()
                .block();

        Flux<Payload> stream = rSocket.requestStream(DefaultPayload.create("Request"));

        stream
                .map(Payload::getDataUtf8)
                .doOnNext(System.out::println)
                .blockLast();

        rSocket.dispose();
    }
}
