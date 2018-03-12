/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.jax.rs.example.sse.server;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.OutboundSseEvent.Builder;

import org.apache.aries.jax.rs.example.sse.Stats;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsExtensionSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsResource;

import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

@Component(service = StatsRestService.class)
@JaxrsApplicationSelect("(osgi.jaxrs.name=sse)")
@JaxrsResource
@JaxrsExtensionSelect("(osgi.jaxrs.name=jackson-json)")
@Path("/api/stats")
public class StatsRestService {
    private static final Random RANDOM = new Random();

    private SseBroadcaster broadcaster;
    private Builder builder;

    @Context
    public void setSse(Sse sse) {
        this.broadcaster = sse.newBroadcaster();
        this.builder = sse.newEventBuilder();
        subscribe(broadcaster, builder);
    }

    @GET
    @Path("broadcast")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void broadcast(@Context SseEventSink sink) {
        broadcaster.register(sink);
    }

    @GET
    @Path("sse")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void stats(@Context SseEventSink sink) {
        Flowable
            .interval(1, TimeUnit.SECONDS)
            .zipWith(eventsStream(builder), (id, bldr) -> createSseEvent(bldr, id))
            .subscribeOn(Schedulers.single())
            .subscribe(sink::send, ex -> {}, sink::close);
    }

    private static void subscribe(final SseBroadcaster broadcaster, final Builder builder) {
        Flowable
            .interval(1, TimeUnit.SECONDS)
            .zipWith(eventsStream(builder), (id, bldr) -> createSseEvent(bldr, id))
            .subscribeOn(Schedulers.single())
            .subscribe(broadcaster::broadcast);
    }

    private static Flowable<OutboundSseEvent.Builder> eventsStream(final Builder builder) {
        return Flowable.generate(emitter -> emitter.onNext(builder.name("stats")));
    }

    private static OutboundSseEvent createSseEvent(final OutboundSseEvent.Builder builder, final long id) {
        return builder
            .id(Long.toString(id))
            .data(Stats.class, new Stats(new Date().getTime(), RANDOM.nextInt(100)))
            .mediaType(MediaType.APPLICATION_JSON_TYPE)
            .build();
    }
}
