package com.example.three;

import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Flux;

public class RSocketServer {
    public static void main(String[] args) {
        RSocket rSocket = new AbstractRSocket() {
            @Override
            public Flux<Payload> requestStream(Payload payload) {
                return Flux.just(
                        DefaultPayload.create("Hello, "),
                        DefaultPayload.create("RSocket "),
                        DefaultPayload.create("World!"));
            }
        };

        RSocketFactory.receive()
                .acceptor((setupPayload, reactiveSocket) -> Mono.just(rSocket))
                .transport(TcpServerTransport.create("localhost", 7000))
                .start()
                .block()
                .onClose()
                .block();
    }
}
