package WolfPack.OrderGeneratorService;

import java.util.ArrayList;

public class OrderList{
    //to keep a list of globally available list of orders with the type Order.class objects
    private static ArrayList<Order> orderList = new ArrayList<Order>();
    //method of adding
    public static void addToList(Order order) {
        orderList.add(order);
    }
    //method of calling
    public static ArrayList<Order> getOrders(){
        return orderList;
    }

    public static Order getOrderById(String id) throws NoSuchOrder {
        for (Order i : orderList ) {
            if(i.getOrderId().equals(id))
            return i;
         }
         throw new NoSuchOrder();
    }
    
    public static void reset(){
        orderList.clear();
    }
};