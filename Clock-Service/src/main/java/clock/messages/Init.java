package clock.messages;

import akka.actor.ActorRef;

public class Init {
    private int delay;
    private ActorRef ref;

    public Init(int delay, ActorRef ref) {
        this.delay = delay;
        this.ref = ref;
    }

    public Init() {}
    
    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public ActorRef getRef() {
        return ref;
    }

    public void setRef(ActorRef ref) {
        this.ref = ref;
    }
}