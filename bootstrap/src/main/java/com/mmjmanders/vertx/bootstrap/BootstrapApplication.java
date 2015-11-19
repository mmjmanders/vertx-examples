package com.mmjmanders.vertx.bootstrap;

import com.mmjmanders.vertx.client.ClientVerticle;
import com.mmjmanders.vertx.sender.MessageSenderVerticle;
import com.mmjmanders.vertx.sockjs.SockJSEventBusBridgeVerticle;
import com.mmjmanders.vertx.stocks.StocksSenderVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Created by 310034430 on 17-11-2015.
 */
public class BootstrapApplication {
    private static final Logger log = LoggerFactory.getLogger(BootstrapApplication.class);

    public static void main(String[] args) {
        Vertx.clusteredVertx(new VertxOptions(), handler -> {
            if (handler.succeeded()) {
                final Vertx vertx = handler.result();
                vertx.deployVerticle(new SockJSEventBusBridgeVerticle(), asyncResult -> log.info("Deployed SockJSEventBusBridgeVerticle with id " + asyncResult.result()));
                vertx.deployVerticle(new MessageSenderVerticle(), asyncResult -> log.info("Deployed MessageSenderVerticle with id " + asyncResult.result()));
                vertx.deployVerticle(new StocksSenderVerticle(), asyncResult -> log.info("Deployed StocksSenderVerticle with id " + asyncResult.result()));
                vertx.deployVerticle(new ClientVerticle(), asyncResult -> log.info("Deployed ClientVerticle with id " + asyncResult.result()));
            } else {
                throw new RuntimeException(handler.cause());
            }
        });
    }
}
