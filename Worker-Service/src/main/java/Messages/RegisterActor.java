package Messages;


import akka.actor.ActorRef;

public class RegisterActor {
    private ActorRef ref;
    private String name;

    public ActorRef getRef() {
        return ref;
    }

    public void setRef(ActorRef ref) {
        this.ref = ref;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RegisterActor(ActorRef ref, String name) {
        this.ref = ref;
        this.name = name;
    }
  

}