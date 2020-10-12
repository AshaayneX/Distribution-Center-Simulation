package WolfPack.OrderGeneratorService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import okhttp3.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class Controller {
    
    private Subscriber s= new Subscriber();
    private Random rand = new Random();
    private Inventory i = new Inventory();

    @Autowired
    private RestTemplate restTemplate;
    //endpoint which recieves the tick from the clock service
    @RequestMapping(value="/generate", method=RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void genarateOrder(@RequestBody Step step) throws IOException {
      if(s.hasSubscriber()){
       if(rand.nextInt(5)==0){//20% chance
      
       Item[] items = restTemplate.getForObject("http://simulator-service/items", Item[].class);
       i.getItemList().clear();
       for(Item p:items){
          i.addItem(p);
       }
         Order o = new Order(step.getStep());
         int nOfItems = rand.nextInt(100);
         if(nOfItems<6){
           o.setitemsInOrder(i.getRandomList(5));
         }
         else if(nOfItems<13){
            o.setitemsInOrder(i.getRandomList(4));
         }
         else if(nOfItems<25){
            o.setitemsInOrder(i.getRandomList(3));
         }
         else if(nOfItems<50){
            o.setitemsInOrder(i.getRandomList(2));
         }
         else{
            o.setitemsInOrder(i.getRandomList(1));
         }
         OrderList.addToList(o);
         //notify management service
         
         String msg = "{\r\n    \"newOrder\" : \""+step.getStep()+"\"\r\n}" ;

         OkHttpClient client = new OkHttpClient().newBuilder()
          .build();
         MediaType mediaType = MediaType.parse("application/json");
         okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType,msg );
         Request request = new Request.Builder()
         .url(s.getUri())
         .method("POST", body)
         .addHeader("Content-Type", "application/json")
         .build();
         Response response = client.newCall(request).execute();
        
         System.out.println("order created");
       }
       else{
         System.out.println("tick recieved but didnt create order");
       }
      }
       else{
         System.out.println("tick recieved but didnt create order");
       }
       
    }

    //endpoint to handle the registration of the services to recieve orders
    @RequestMapping(value="/registry", method=RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void registerService(@RequestBody Subscriber s2) {
       System.out.println("Registraion request received");
       s.setUri(s2.getUri());
    }

    //endpoint to obtain a list of orders
    @RequestMapping(value="/orders", method=RequestMethod.GET)
    public ArrayList<Order> showOrders() {
         return OrderList.getOrders();
    }

    //endpoint to obtain a details of a specific order
    @RequestMapping(value="/orders/{orderID}", method=RequestMethod.GET)
    public Order getOrderDetails(@PathVariable String orderID) throws NoSuchOrder {
       //  ArrayList<Order> tempArrayList = OrderList.getOrders();
       return OrderList.getOrderById(orderID);
    
      }
   
   @RequestMapping(value="/orders/{orderID}", method=RequestMethod.PUT)
    public void gsetOrderStatus(@PathVariable String orderID, @RequestBody Status s) throws NoSuchOrder {
         OrderList.getOrderById(orderID).setStatus(s.getType());
    
      }
   
      @RequestMapping(value="/reset", method=RequestMethod.POST)
      public void reset() {
           OrderList.reset();
           s.setUri("");
        }
  

   
   

      
}