package com.cffex.chapter8.serverside;

import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.IncomingConnection;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpMethods;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.Uri;
import akka.japi.Function;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.stream.javadsl.Tcp;

import java.util.concurrent.CompletionStage;


/**
 * Created by Ming on 2016/6/13.
 */
public class LowLevelApi {
    public static void main(String[] args)throws Exception {
        ActorSystem system = ActorSystem.create();
        Materializer materializer = ActorMaterializer.create(system);

        Source<IncomingConnection, CompletionStage<ServerBinding>> serverSource =
            Http.get(system).bind(ConnectHttp.toHost("localhost", 8080), materializer);

        CompletionStage<ServerBinding> serverBindingFuture =
            serverSource.to(Sink.foreach(connection -> {
                    System.out.println("Accepted new connection from " + connection.remoteAddress());
                    // ... and then actually handle the connection
                }
            )).run(materializer);
    }
}
