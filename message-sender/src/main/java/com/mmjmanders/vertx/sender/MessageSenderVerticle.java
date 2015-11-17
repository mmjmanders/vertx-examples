package com.mmjmanders.vertx.sender;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

import java.util.Random;

/**
 * Created by 310034430 on 17-11-2015.
 */
public class MessageSenderVerticle extends AbstractVerticle {

    private static final Random random = new Random();

    @Override
    public void start() throws Exception {
        vertx.setPeriodic(1000, handler ->
            vertx.eventBus().publish("random", new JsonObject()
                .put("date", System.currentTimeMillis())
                .put("value", random.nextInt(101))
            )
        );
    }
}
