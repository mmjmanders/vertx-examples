package com.mmjmanders.vertx.springboot;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Created by 310034430 on 19-11-2015.
 */
@SpringBootApplication
@Component
public class SpringBootVertxApplication implements CommandLineRunner {

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        Vertx.clusteredVertx(new VertxOptions(), handler -> {
            if (handler.succeeded()) {
                final Vertx vertx = handler.result();
                vertx.setPeriodic(1500, longHandler ->
                    vertx.eventBus().publish("random", new JsonObject()
                        .put("date", System.currentTimeMillis())
                        .put("value", random.nextInt(101))
                    )
                );
            } else {
                throw new RuntimeException(handler.cause());
            }
        });
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringBootVertxApplication.class).bannerMode(Banner.Mode.OFF).web(false).run(args);
    }
}
