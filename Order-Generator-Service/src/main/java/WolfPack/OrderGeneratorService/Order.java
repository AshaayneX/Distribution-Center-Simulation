package WolfPack.OrderGeneratorService;

import java.util.ArrayList;

public class Order {

    private String orderId ;
    private String status;
    
    ArrayList <Item> itemsInOrder = new ArrayList<Item>();
    //this array list holds the itemIDs of the items of each specific order as strings
    
    //Constructor for creating an order
    //order id is a must for orders
    public Order(String orderId) {
        this.status="NEW";
        this.orderId=orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public void addItem(Item itemID) {
        this.itemsInOrder.add(itemID);
    }

    public String getStatus() {
      return this.status;
    }

    public ArrayList<Item> getitemsInOrder() {
        return itemsInOrder;
    }

    public void setitemsInOrder(ArrayList<Item> itemsInOrder) {
        this.itemsInOrder = itemsInOrder;
    }

    public void setStatus(String status) {
        this.status = status;
    }
   
};