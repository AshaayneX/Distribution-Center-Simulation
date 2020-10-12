package clock;

import akka.actor.AbstractActor;
import akka.actor.Props;
import clock.messages.Init;
import clock.messages.RequestStep;
import clock.messages.Step;


public class Clock extends AbstractActor {
    public static Props props() {
        return Props.create(Clock.class);
    }

    private int step = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder().
        match(Init.class,
        msg -> {
            Thread.sleep(msg.getDelay());
            System.out.println("[Clock] step: " + step);
            msg.getRef().tell(new Step(step), getSelf());
            step++;
            getSelf().tell(msg, null);
        }).match(RequestStep.class,
        msg -> {
            getSender().tell(new Step(step), getSelf());
        }).build();
    }
    
}