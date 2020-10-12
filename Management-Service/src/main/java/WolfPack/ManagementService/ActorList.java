package WolfPack.ManagementService;

import java.util.ArrayList;

public class ActorList{
    private static ArrayList <Actor> actorList = new ArrayList<Actor>();

    static void addToList(Actor a){
        actorList.add(a);
    }

    public static ArrayList<Actor> getActorList() {
        return actorList;
    }

    public static void setActorList(ArrayList<Actor> actorList) {
        ActorList.actorList = actorList;
    }

    public static Actor getActorbyName(String name) throws Exception {
        for(Actor a:actorList){
            if(a.getName().equals(name))
            return a;
        }
        throw new Exception("No such actor");
    }

    public static void setActorsAttributes(){
        for(Actor a :actorList){
            a.setAttributes();
        }
    }

    public static void reset(){
       actorList.clear();
    }
}