package com.mmjmanders.vertx.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * Created by mark on 17-11-2015.
 */
public class ClientVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        Launcher.main(new String[]{"run", ClientVerticle.class.getName(), "--ha"});
    }

    @Override
    public void start() throws Exception {
        final Router router = Router.router(vertx);
        router.route(HttpMethod.GET, "/*").handler(StaticHandler.create().setDirectoryListing(false).setIndexPage("index.html"));

        vertx.createHttpServer().requestHandler(router::accept).listen(9000);
    }
}
