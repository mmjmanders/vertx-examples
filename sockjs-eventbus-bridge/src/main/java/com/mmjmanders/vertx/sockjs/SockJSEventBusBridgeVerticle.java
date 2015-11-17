package com.mmjmanders.vertx.sockjs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

/**
 * Created by 310034430 on 17-11-2015.
 */
public class SockJSEventBusBridgeVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        final Router router = Router.router(vertx);

        router.route("/eventbus/*").method(HttpMethod.GET).handler(
                SockJSHandler.create(vertx).bridge(
                        new BridgeOptions().addOutboundPermitted(new PermittedOptions().setAddress("random"))
                )
        );

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }
}
