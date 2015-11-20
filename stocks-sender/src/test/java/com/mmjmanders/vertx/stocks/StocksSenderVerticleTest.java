package com.mmjmanders.vertx.stocks;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by mark on 20/11/15.
 */
@RunWith(VertxUnitRunner.class)
public class StocksSenderVerticleTest {

    Vertx vertx;

    @Before
    public void setup(TestContext context) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(new StocksSenderVerticle(), context.asyncAssertSuccess());
    }

    @After
    public void after(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testStocksSender(TestContext context) {
        Async async = context.async();
        vertx.eventBus().consumer("stocks", message -> {
            Object body = message.body();
            context.assertNotNull(body, "No message received");
            context.assertTrue(body instanceof JsonObject, "Received message is not of type io.vertx.core.json.JsonObject");

            JsonObject jsonObject = (JsonObject) body;
            JsonObject query = jsonObject.getJsonObject("query");
            int count = query.getInteger("count");
            context.assertTrue(count > 0, "No stock quotes received");
            context.assertEquals(count, query.getJsonObject("results").getJsonArray("row").size(), "Count doesn't match actual number of stock quotes");

            async.complete();
        });
        async.await(20_000);
    }
}
