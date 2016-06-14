package com.cffex.chapter8.clientside;

import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.HostConnectionPool;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.japi.Pair;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import scala.util.Try;

import java.util.concurrent.CompletionStage;

/**
 * Created by Ming on 2016/6/9.
 */
public class HostLevel {
    public static void main(String[] args) throws Exception {
        final ActorSystem system = ActorSystem.create();
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        // construct a pool client flow with context type `Integer`
        final Flow<
                Pair<HttpRequest, Integer>,
                Pair<Try<HttpResponse>, Integer>,
                HostConnectionPool> poolClientFlow =
                Http.get(system).<Integer>cachedHostConnectionPool(ConnectHttp.toHost("akka.io", 80), materializer);

        // construct a pool client flow with context type `Integer`
        final CompletionStage<Pair<Try<HttpResponse>, Integer>> responseFuture =
                Source.single(Pair.create(HttpRequest.create("/"), 42))
                    .via(poolClientFlow)
                    .runWith(Sink.<Pair<Try<HttpResponse>, Integer>>head(), materializer);
    }
}
