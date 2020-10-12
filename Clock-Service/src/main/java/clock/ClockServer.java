package clock;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import clock.messages.Init;
import clock.messages.Registration;
import clock.messages.RegistrationList;
import clock.messages.RequestRegistrations;
import clock.messages.RequestStep;
import clock.messages.Result;
import clock.messages.Step;

public class ClockServer extends AllDirectives {
    private Duration TIMEOUT = Duration.ofSeconds(5l);

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("clock-service");

        Http http = Http.get(system);
        ActorMaterializer materializer = ActorMaterializer.create(system);

        ClockServer app = new ClockServer(system);

        Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);

        http.bindAndHandle(routeFlow, ConnectHttp.toHost("localhost", 9000), materializer);
        System.out.println("Server started on port: 9000");
    }

    private ActorRef broker;
    private ActorRef clock;

    public ClockServer(ActorSystem system) {
        broker = system.actorOf(Broker.props());
        clock = system.actorOf(Clock.props());
        clock.tell(new Init(5000, broker), null);
    }

    protected Route createRoute() {
        return route(
            pathPrefix("registry", () -> getRegistryRoutes()),
            pathPrefix("clock", () -> getClockRoutes())
        );
    }

    private Route getRegistryRoutes() {
        return route(
            post(() -> 
                entity(Jackson.unmarshaller(Registration.class), registration -> {
                    CompletionStage<Result> register
                        = Patterns.ask(broker, 
                            registration, TIMEOUT
                                ).thenApply(Result.class::cast);

                    return onSuccess(() -> register,
                            msg ->
                                complete(
                                    StatusCodes.OK, 
                                    msg, Jackson.marshaller()
                    ));
                })
            ),
            get(() -> {
                CompletionStage<RegistrationList> request
                    = Patterns.ask(broker, 
	                     new RequestRegistrations(), TIMEOUT
                    		).thenApply(RegistrationList.class::cast);

                return onSuccess(() -> request,
                        msg ->
                            complete(
                                StatusCodes.OK, 
                                msg, Jackson.marshaller()
                ));
            })
        );
    }

    private Route getClockRoutes() {
        return route(
            get(() -> {
                CompletionStage<Step> request
                    = Patterns.ask(clock, 
	                     new RequestStep(), TIMEOUT
                    		).thenApply(Step.class::cast);

                return onSuccess(() -> request,
                        msg ->
                            complete(
                                StatusCodes.OK, 
                                msg, Jackson.marshaller()
                ));
            })
        );
    }
}