package org.apache.aries.jax.rs.example.sse.client;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.SseEventSource;

import org.apache.aries.jax.rs.example.sse.Stats;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class SseClientStarter {
    public static void main(String[] args) throws InterruptedException {
        final WebTarget target = ClientBuilder
            .newClient()
            .register(JacksonJsonProvider.class)
            .target("http://localhost:8080/sse/api/stats/sse");

        try (final SseEventSource eventSource =
                     SseEventSource.target(target)
                                   .reconnectingEvery(5, TimeUnit.SECONDS)
                                   .build()) {

            eventSource.register(event -> {
                final Stats stats = event.readData(Stats.class, MediaType.APPLICATION_JSON_TYPE);
                System.out.println("name: " + event.getName());
                System.out.println("id: " + event.getId());
                System.out.println("comment: " + event.getComment());
                System.out.println("data: " + stats.getLoad() + ", " + stats.getTimestamp());
                System.out.println("---------------");
            });
            eventSource.open();

            // Just consume SSE events for 10 seconds
            Thread.sleep(10000);
        }
    }
}
