package WolfPack.ManagementService;

import java.util.ArrayList;

public class OrderList {
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

    public static Order getNextOrder(){
        return orderList.get(0);
    }

    public static Boolean hasMoreOrders(){
      
        return !orderList.isEmpty();
    }
    
    public static void orderComplete(Order o){
      orderList.remove(o);
    }

    public static void reset(){
        orderList.clear();
      }

  

};