package com.mmjmanders.vertx.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * Created by 310034430 on 17-11-2015.
 */
public class ClientVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        final Router router = Router.router(vertx);
        router.route("/*").handler(StaticHandler.create().setDirectoryListing(false).setIndexPage("index.html"));

        vertx.createHttpServer().requestHandler(router::accept).listen(9000);
    }
}
