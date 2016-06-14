package com.cffex.chapter8.clientside;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.OutgoingConnection;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

import java.util.concurrent.CompletionStage;

/**
 * Created by Ming on 2016/6/9.
 */
public class ConnectionLevel {
    public static void main(String[] args) throws Exception{
        final ActorSystem system = ActorSystem.create();
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        final Flow<HttpRequest, HttpResponse, CompletionStage<OutgoingConnection>> connectionFlow =
                Http.get(system).outgoingConnection("akka.io");
        final CompletionStage<HttpResponse> responseFuture =
                Source.single(HttpRequest.create("/"))
                        .via(connectionFlow)
                        .runWith(Sink.<HttpResponse>head(), materializer);
    }
}
