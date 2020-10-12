import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.pattern.Patterns;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import okhttp3.*;
import akka.http.javadsl.server.Route;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletionStage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import Messages.GiveOrderToActor;
import Messages.RegisterActor;
import Messages.Reset;
import Messages.Result;
import Messages.Step;
import Messages.PatchWorker;;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")
public class WorkerService extends AllDirectives {

    @Bean
    public RestTemplate getRest() {
        return new RestTemplate();
    }

    private static RestTemplate restTemplate = new RestTemplate();

    private static String[] workers;
    private static ActorRef broker;
    private static ActorSystem system;
    private Duration TIMEOUT = Duration.ofSeconds(5);
    private Duration TIMEOUT2 = Duration.ofSeconds(10);
    // private static OkHttpClient client = new OkHttpClient().newBuilder().build();
    // private static MediaType mediaType = MediaType.parse("application/json");

    public WorkerService(ActorSystem system) {
        broker = system.actorOf(Props.create(Broker.class), "broker"); 
        registerWorkers();

    }

    public static void registerWorkers() {
        
        workers = restTemplate.getForObject("http://localhost:8082/workers", String[].class);
        for (String names : workers) {
            ActorRef actor = system.actorOf(Props.create(WorkerBrain.class), names);
            broker.tell(new RegisterActor(actor, names), null);
            actor.tell(new PatchWorker(), null);
            // registering in the management service

             OkHttpClient client = new OkHttpClient().newBuilder()
             .build();
             MediaType mediaType = MediaType.parse("application/json");
             RequestBody body = RequestBody.create(mediaType, "{ \"name\" : \""+names+"\" \r\n}");
             Request request = new Request.Builder()
             .url("http://localhost:8086/registry")
             .method("POST", body)
             .addHeader("Content-Type", "application/json")
             .build();
             try {
             Response response = client.newCall(request).execute();
             } catch (IOException e) {
            
             e.printStackTrace();
             }
        }
    }

    public static void main(String[] args) {

        system = ActorSystem.create("worker-service");

        Http http = Http.get(system);
        ActorMaterializer materializer = ActorMaterializer.create(system);

        WorkerService app = new WorkerService(system);

        Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);

        http.bindAndHandle(routeFlow, ConnectHttp.toHost("localhost", 8090), materializer);
        System.out.println("Server started on port: 8090");

         clockRegistration();
    }

    private Route createRoute() {
        return route(pathPrefix("reset", () -> reset()), pathPrefix("handler", () -> getActor()),
                pathPrefix("clock", () -> nextStep()));
    }

    private Route reset() {
        return route(post(() -> entity(Jackson.unmarshaller(Reset.class), reset -> {

            CompletionStage<Result> resetMsg = Patterns.ask(broker, reset, TIMEOUT2).thenApply(Result.class::cast);
            
            registerWorkers();
            System.out.println("Server Resetted");
            return onSuccess(() -> resetMsg, msg -> complete(StatusCodes.OK, msg, Jackson.marshaller()));
        })));
    }

    private Route getActor() {
        return route(post(() -> entity(Jackson.unmarshaller(GiveOrderToActor.class), order -> {
            System.out.println("Recieved Order");
            CompletionStage<Result> register = Patterns.ask(broker, order, TIMEOUT).thenApply(Result.class::cast);

            return onSuccess(() -> register, msg -> complete(StatusCodes.OK, msg, Jackson.marshaller()));
        })));
    }

    private Route nextStep() {
        return route(put(() -> {
            CompletionStage<Result> request = Patterns.ask(broker, new Step(), TIMEOUT).thenApply(Result.class::cast);

            return onSuccess(() -> request, msg -> complete(StatusCodes.OK, msg, Jackson.marshaller()));
        }));
    }

    public static void clockRegistration() {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");


        RequestBody body = RequestBody.create(mediaType,
                "{ \"name\" : \"Worker\", \"uri\" : \"http://localhost:8090/clock\"}");
        Request request = new Request.Builder().url("http://localhost:9000/registry").method("POST", body)
                .addHeader("Content-Type", "application/json").build();
        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
        
            e.printStackTrace();
        }
          
    }
}
