package WolfPack.ManagementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

public class NewOrder {
    
    private String newOrder ;
    
    @LoadBalanced
    private RestTemplate restTemplate = new RestTemplate();

    public void addOrderToQueue(){
      
        String url = "http://localhost:8083/orders/"+newOrder;
        Order o = restTemplate.getForObject(url, Order.class);
        OrderList.addToList(o);   
    }

    public String getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(String newOrder) {
        this.newOrder = newOrder;
    }
}