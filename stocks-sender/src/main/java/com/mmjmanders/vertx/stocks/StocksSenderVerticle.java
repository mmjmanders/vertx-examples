package com.mmjmanders.vertx.stocks;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.TimeUnit;

/**
 * Created by mark on 19-11-2015.
 */
public class StocksSenderVerticle extends AbstractVerticle {

    private static final String HOST = "query.yahooapis.com";
    private static final String URI = "/v1/public/yql?q=select%20*%20from%20csv%20where%20url%3D%27http%3A%2F%2Fdownload.finance.yahoo.com%2Fd%2Fquotes.csv%3Fs%3DPHG%2CAAPL%2CASML%26f%3Dsl1d1t1c1ohgv%26e%3D.csv%27%20and%20columns%3D%27symbol%2Cprice%2Cdate%2Ctime%2Cchange%2Ccol1%2Chigh%2Clow%2Ccol2%27&format=json";

    public static void main(String[] args) {
        Launcher.main(new String[]{"run", StocksSenderVerticle.class.getName(), "--ha"});
    }

    @Override
    public void start() throws Exception {
        vertx.setPeriodic(TimeUnit.MILLISECONDS.convert(60, TimeUnit.MINUTES), handler ->
            vertx.createHttpClient(new HttpClientOptions().setDefaultHost(HOST)).getNow(URI, response ->
                response.bodyHandler(bodyHandler ->
                    vertx.eventBus().publish("stocks", new JsonObject(bodyHandler.getString(0, bodyHandler.length())))
                )
            )
        );
    }
}
