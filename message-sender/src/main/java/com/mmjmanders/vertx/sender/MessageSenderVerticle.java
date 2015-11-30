package com.mmjmanders.vertx.sender;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.json.JsonObject;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by mark on 17-11-2015.
 */
public class MessageSenderVerticle extends AbstractVerticle {

    private final Random random = new Random();

    public static void main(String[] args) {
        Launcher.main(new String[]{"run", MessageSenderVerticle.class.getName(), "--ha"});
    }

    @Override
    public void start() throws Exception {
        vertx.setPeriodic(TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS), handler ->
            vertx.eventBus().publish("random", new JsonObject()
                .put("date", System.currentTimeMillis())
                .put("value", random.nextInt(101))
            )
        );
    }
}
