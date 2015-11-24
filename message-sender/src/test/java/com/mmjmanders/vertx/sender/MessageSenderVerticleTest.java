package com.mmjmanders.vertx.sender;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

/**
 * Created by mark on 20/11/15.
 */
@RunWith(VertxUnitRunner.class)
public class MessageSenderVerticleTest {

    Vertx vertx;

    @Before
    public void setup(TestContext context) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(new MessageSenderVerticle(), context.asyncAssertSuccess());
    }

    @After
    public void after(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testMessageSender(TestContext context) {
        Async async = context.async();
        vertx.eventBus().consumer("random", message -> {
            Object body = message.body();
            context.assertNotNull(body, "No message received");
            context.assertTrue(body instanceof JsonObject, "Received message is not of type io.vertx.core.json.JsonObject");

            JsonObject jsonObject = (JsonObject) body;
            long date = jsonObject.getLong("date");
            int value = jsonObject.getInteger("value");
            context.assertTrue(date > 0, "Invalid date received");
            context.assertTrue(value >= 0 && value <= 100, "Invalid value received");

            async.complete();
        });
        async.await(TimeUnit.MILLISECONDS.convert(10, TimeUnit.SECONDS));
    }
}
