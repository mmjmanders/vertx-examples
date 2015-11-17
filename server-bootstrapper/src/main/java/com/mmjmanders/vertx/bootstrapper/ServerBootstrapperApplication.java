package com.mmjmanders.vertx.bootstrapper;

import com.mmjmanders.vertx.sender.MessageSenderVerticle;
import com.mmjmanders.vertx.sockjs.SockJSEventBusBridgeVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Created by 310034430 on 17-11-2015.
 */
public class ServerBootstrapperApplication {
    private static final Logger log = LoggerFactory.getLogger(ServerBootstrapperApplication.class);

    public static void main(String[] args) {
        final Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new SockJSEventBusBridgeVerticle(), asyncResult -> log.info("Deployed SockJSEventBusBridgeVerticle with id " + asyncResult.result()));
        vertx.deployVerticle(new MessageSenderVerticle(), asyncResult -> log.info("Deployed MessageSenderVerticle with id " + asyncResult.result()));
    }
}
