package WolfPack.ManagementService;

import java.util.ArrayList;

public class Order {

    private String orderId ;
    private String status;
    
    private ArrayList <Item> itemsInOrder = new ArrayList<Item>();

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getWeight(){
        int weight = 0;
        for(Item i :itemsInOrder){
            int w=Integer.parseInt(i.getWeight());
            weight+= i.getQuantity()*w;
        }
        return weight;
    }

    public ArrayList<Item> getItemsInOrder() {
        return itemsInOrder;
    }

    public void setItemsInOrder(ArrayList<Item> itemsInOrder) {
        this.itemsInOrder = itemsInOrder;
    }
    
    public String[] getitemids(){
        String[] temp = new String[itemsInOrder.size()];
        for(int i = 0; i<itemsInOrder.size();i++){
            temp[i]= itemsInOrder.get(i).getId();
        }
        return temp;
    }
}   