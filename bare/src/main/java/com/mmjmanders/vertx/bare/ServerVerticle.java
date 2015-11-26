package com.mmjmanders.vertx.bare;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;

import java.lang.management.ManagementFactory;

/**
 * Created by mark on 26/11/15.
 */
public class ServerVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        Launcher.main(new String[]{"run", ServerVerticle.class.getName(), "--ha"});
    }

    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(req -> {
            req.response().putHeader("Content-Type", "text/plain").end(String.format("Running at %s", ManagementFactory.getRuntimeMXBean().getName()));
        }).listen(8000);
    }
}
