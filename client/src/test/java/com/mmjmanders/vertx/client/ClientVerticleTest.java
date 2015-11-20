package com.mmjmanders.vertx.client;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
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
public class ClientVerticleTest {

    Vertx vertx;

    @Before
    public void setup(TestContext context) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(new ClientVerticle(), context.asyncAssertSuccess());
    }

    @After
    public void after(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testClient(TestContext context) {
        Async async = context.async();
        vertx.createHttpClient().getNow(9000, "localhost", "/index.html", response -> {
            if (response.statusCode() == HttpResponseStatus.OK.code()) {
                context.assertEquals(response.headers().get("Content-Type"), "text/html", "Response is not of type text/html");

                async.complete();
            } else context.fail();
        });
        async.await(10_000);
    }
}
