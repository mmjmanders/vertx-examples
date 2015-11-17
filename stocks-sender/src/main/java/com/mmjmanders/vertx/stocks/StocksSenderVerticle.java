package com.mmjmanders.vertx.stocks;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Created by 310034430 on 17-11-2015.
 */
public class StocksSenderVerticle extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(StocksSenderVerticle.class);

    @Override
    public void start() throws Exception {
        vertx.setPeriodic(5000, handler -> vertx.createHttpClient().getNow(443, "finance.google.com", "/finance/info?client=ig&q=AEX:PHIA,AEX:ASML", response ->
                response.bodyHandler(bodyHandler -> vertx.eventBus().publish("stocks", new JsonArray(bodyHandler.getBuffer(0, bodyHandler.length()).toString().replaceAll("//", ""))))
        ));
    }
}
