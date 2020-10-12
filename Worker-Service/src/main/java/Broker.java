import java.util.HashMap;
import java.util.Map;

import Messages.Die;
import Messages.GiveOrderToActor;
import Messages.NotifyWorker;
import Messages.PerformOrder;
import Messages.Step;
import Messages.RegisterActor;
import Messages.Reset;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import Messages.Result;

public class Broker extends AbstractActor {

    private Map<String, ActorRef> actors = new HashMap<String, ActorRef>();
    private NotifyWorker notify = new NotifyWorker();

    public Receive createReceive() {
        return receiveBuilder().
            match(RegisterActor.class,
            msg -> {
                System.out.println("registered actor"+msg.getName());
                actors.put(msg.getName(), msg.getRef());
            }).
            match(GiveOrderToActor.class,
            msg -> {
                ActorRef target = actors.get(msg.getActor());
                if (target != null) {
                  target.tell(new PerformOrder(msg.getPackAtLocation(),msg.getItemIDs(), msg.getOrderID()),getSelf());
                }
                
                getSender().tell(new Result(), getSelf());
            }).
            match(Reset.class,
            msg -> {
                for (Map.Entry<String, ActorRef> set : actors.entrySet()) {
                    set.getValue().tell(new Die(),getSelf());
                }
                actors.clear();  
                getSender().tell(new Result(), getSelf());
            }).
            match(Step.class,
            msg -> {
                for (Map.Entry<String, ActorRef> set : actors.entrySet()) {
                    set.getValue().tell(notify,getSelf());
                }
                getSender().tell(new Result(), getSelf());
            }).build();
    }
    
}
