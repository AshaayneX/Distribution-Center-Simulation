package WolfPack.SimulatorService;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class Worker {
   
    private String name;
    private String location;
    private String capacity;
    private ArrayList<Action> actions = new ArrayList<Action>(); ;
	private Action nextAction = null;
	private String notificationUri = "";
	private ArrayList<String> holdingItems = new ArrayList<String>();

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
  
    public String getNotificationUri() {
        return notificationUri;
    }

    public void setNotificationUri(String notificationUri) {
        this.notificationUri = notificationUri;
    }
    
    public void move(String vertecId){
        this.location = vertecId;
    }

    public void pick(String itemID) throws NoSuchItem {
        this.holdingItems.add(itemID);
    }
    
    public void drop(String itemId) throws NoSuchItem, NoSuchVertex {
        VertexList.getVertexByName(this.location).addItem(ItemList.getItemById(itemId));
        this.holdingItems.remove(itemId);
    }

    public void pack(String orderId) throws NoSuchVertex {
        VertexList.getVertexByName(this.location).packOrder();
    }

    public ArrayList<String> getHoldingItems() {
        return holdingItems;
    }

    public void setHoldingItems(ArrayList<String> holdingItems) {
        this.holdingItems = holdingItems;
    }

    public Action getNextAction() {
        return nextAction;
    }

    public void setNextAction(Action nextAction) {
        this.nextAction = nextAction;
    }

    public void work() throws NoSuchItem, NoSuchVertex {
        if(nextAction.equals(null)){
        }
        else{
            switch(nextAction.getType()){
                case "move" :
                  move(nextAction.getArguments());
                  break;

                case "pick" :
                 pick(nextAction.getArguments());
                  break;

                case "drop" :
                  drop(nextAction.getArguments());
                  break;  

                case "pack" :
                  pack(nextAction.getArguments());
                  break;
            }
            nextAction.setSuccess(true);
            nextAction.setStep(Step.getDummy());
            actions.add(nextAction);
            nextAction=null;
        }
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }
}
