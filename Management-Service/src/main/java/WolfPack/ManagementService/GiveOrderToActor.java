package WolfPack.ManagementService;

public class GiveOrderToActor {
    private String actor;
    private String orderID;
    private String[] itemIDs;
    private String packAtLocation;

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String[] getItemIDs() {
        return itemIDs;
    }

    public void setItemIDs(String[] itemIDs) {
        this.itemIDs = itemIDs;
    }

    public String getPackAtLocation() {
        return packAtLocation;
    }

    public void setPackAtLocation(String packAtLocation) {
        this.packAtLocation = packAtLocation;
    }

    public GiveOrderToActor(String actor, String orderID, String[] itemIDs, String packAtLocation) {
        this.actor = actor;
        this.orderID = orderID;
        this.itemIDs = itemIDs;
        this.packAtLocation = packAtLocation;
    }

   

  

}