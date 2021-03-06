package com.mmjmanders.vertx.sockjs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

/**
 * Created by mark on 17-11-2015.
 */
public class SockJSEventBusBridgeVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        Launcher.main(new String[]{"run", SockJSEventBusBridgeVerticle.class.getName(), "--ha"});
    }

    @Override
    public void start() throws Exception {
        final Router router = Router.router(vertx);

        router.route(HttpMethod.GET, "/eventbus/*").handler(
            SockJSHandler.create(vertx).bridge(
                new BridgeOptions().addOutboundPermitted(new PermittedOptions().setAddress("random"))
                    .addOutboundPermitted(new PermittedOptions().setAddress("stocks"))
            )
        );

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }
}
