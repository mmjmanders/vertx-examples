package com.mmjmanders.vertx.sockjs;

import io.netty.handler.codec.http.HttpResponse;
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
 * Created by mark on 24/11/15.
 */
@RunWith(VertxUnitRunner.class)
public class SockJSEventBusBridgeVerticleTest {

    private Vertx vertx;

    @Before
    public void setup(TestContext context) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(new SockJSEventBusBridgeVerticle(), context.asyncAssertSuccess());
    }

    @After
    public void after(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testSockJSEventBusBridge(TestContext context) {
        Async async = context.async();
        vertx.createHttpClient().getNow(8080, "localhost", "/eventbus/", response -> {
            if (response.statusCode() == HttpResponseStatus.OK.code()) {
                response.bodyHandler(bodyHandler -> {
                    String body = bodyHandler.getString(0, bodyHandler.length());
                    context.assertNotNull(body, "No response received");
                    context.assertTrue(body.contains("SockJS"), "Response is not from SockJS");
                });
                async.complete();
            } else context.fail();
        });
        async.await(10_000);
    }
}
