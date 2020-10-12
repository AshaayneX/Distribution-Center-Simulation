package clock;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import akka.actor.AbstractActor;
import akka.actor.Props;
import clock.messages.Registration;
import clock.messages.RegistrationList;
import clock.messages.RequestRegistrations;
import clock.messages.Result;
import clock.messages.Step;
import clock.util.RestClient;
import clock.util.WebResponse;


public class Broker extends AbstractActor {
    public static Props props() {
        return Props.create(Broker.class);
    }

    List<Registration> registrations = new LinkedList<Registration>();
    RestClient restClient = new RestClient();
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public Receive createReceive() {
        return receiveBuilder().
        match(Registration.class,
        registration -> {
            registrations.add(registration);
            getSender().tell(new Result(), getSelf());
        }).
        match(RequestRegistrations.class,
        msg -> {
            getSender().tell(new RegistrationList(registrations), getSelf());
        }).
        match(Step.class,
        msg -> {
            String json = mapper.writeValueAsString(msg);
            for (Registration registration : registrations) {
                if (!registration.isFailed()) {
                    System.out.println("Sending to: " + registration.getUri() + " [" + registration.getName() + "]");
                    WebResponse response= restClient.put(registration.getUri(), RestClient.JSON_TYPE, json);
                    if (response == null) registration.setFailed(true);
                }
            }
        })
        .build();
    }
    
}