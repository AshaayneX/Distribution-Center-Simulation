package WolfPack.ManagementService;

import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.client.RestTemplate;

import com.google.gson.*;

import okhttp3.*;

public class Processing {
    
    @LoadBalanced
    private static RestTemplate restTemplate = new RestTemplate();
    
    private static Item[] items = restTemplate.getForObject("http://localhost:8082/items", Item[].class);
    private static String[] packaging  = restTemplate.getForObject("http://localhost:8082/map/packaging", String[].class);
    private static String[] verticeNames = restTemplate.getForObject("http://localhost:8082/map/vertices", String[].class);
    private static HashMap<String, Integer> hm = new HashMap<String, Integer>();
    private static ArrayList<String> nearestAvailableShelf = new ArrayList<String>();
    // private static OkHttpClient client = new OkHttpClient().newBuilder().build();
    // private static MediaType mediaType = MediaType.parse("application/json");
    private static HashMap<String,Boolean> packagingPoints = new HashMap<String, Boolean>();
    
    public static void registerForOrders() {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"uri\": \"http://localhost:8086/newOrder\"\r\n}");
        Request request = new Request.Builder().url("http://localhost:8083/registry").method("POST", body)
                .addHeader("Content-Type", "application/json").build();
        try {
            Response response = client.newCall(request).execute();
        } catch (Exception e) {

            e.printStackTrace();
        }

    }


    public static void allocateItemLocations() {

        String uri;
        // Allocating a value for the vertices based on their distance from the nearest
        // packaging point
        for (String name : verticeNames) {
            int i = 999999999;
            for (String p : packaging) {
                uri = "http://localhost:8082/map/" + p + "/" + name;
                String sux = restTemplate.getForObject(uri, String.class);
                int temp= Integer.valueOf(sux);
                if (temp < i)
                    i = temp;
            }
            hm.put(name, i);
        }
        // sorting the vertices to get the list of vertices which are nearest to a
        // packaging point
        Map<String, Integer> hm1 = sortByValue(hm);
        String[] shelves;

        // generating a list of shelves from the nearest vertices
        for (Map.Entry<String, Integer> en : hm1.entrySet()) {

            shelves = restTemplate.getForObject("http://localhost:8082/map/vertices/" + en.getKey() + "/shelf", String[].class);
            if (shelves.length == 0)
                continue;
            for (String loc : shelves)
                nearestAvailableShelf.add(loc);
            if (nearestAvailableShelf.size() >= items.length)
                break;
        }
        // adding items to shelves
        for (Item it : items) {
            uri = "http://localhost:8082/items/" + it.getId() + "/" + nearestAvailableShelf.get(0);
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder().url(uri).method("POST", body).build();
            try {
                Response response = client.newCall(request).execute();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            nearestAvailableShelf.remove(0);
        }

    }

    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) 
   { 
       // Create a list from elements of HashMap 
       List<Map.Entry<String, Integer> > list = 
              new LinkedList<Map.Entry<String, Integer> >(hm.entrySet()); 
 
       // Sort the list 
       Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
           public int compare(Map.Entry<String, Integer> o1,  
                              Map.Entry<String, Integer> o2) 
           { 
               return (o1.getValue()).compareTo(o2.getValue()); 
           } 
       }); 
         
       // put data from sorted list to hashmap  
       HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>(); 
       for (Map.Entry<String, Integer> aa : list) { 
           temp.put(aa.getKey(), aa.getValue()); 
       } 
       return temp; 
   } 

   public static void setPackingMap(){
       for(String p: packaging){
           packagingPoints.put(p,true);
       }
   }

   public static boolean packingNodeAvailable(){
       for(Map.Entry<String,Boolean> e:packagingPoints.entrySet()){
           if(e.getValue())
           return true;
       }
       return false;
   }

   public static String getNextAvailableNode() throws Exception {
    for(Map.Entry<String,Boolean> e:packagingPoints.entrySet()){
        if(e.getValue())
        return e.getKey();
    }
    throw new Exception("no more available nodes");
}  
    public static void makeNodeAvailable(String key){
        packagingPoints.replace(key, false, true);
      }
    
   public static void processOrder() throws Exception {
    System.out.println("Process Started");
       if(packingNodeAvailable()){
        System.out.println("Packing node available ");
           ArrayList<Order>comOrders  = new ArrayList<Order>();
           int orderList = OrderList.getOrders().size();
           int actorList = ActorList.getActorList().size();
           System.out.println("actorList size" + actorList);
       for( int i=0;i<orderList;i++){
        System.out.println("iterating over orders");
          Order o = OrderList.getOrders().get(i);
          int leftOverCapacity = 999999999;
          Actor chosenActor = new Actor();
          boolean allocated = false;
          int cap;
          System.out.println("end of iterating over orders" + o.getOrderId());
          for(int j=0;j<actorList;j++){
            System.out.println("iterating over available actors");
              Actor a = ActorList.getActorList().get(j);
              cap = Integer.parseInt(a.getCapacity());
              if(cap <o.getWeight()||a.getStatus().equals("Working")){
              continue;
              }
              else{
                  int temp = cap-o.getWeight();
                  if(temp<leftOverCapacity){
                      leftOverCapacity=temp;
                      chosenActor=a;
                      allocated =true;
                  }                    
              }
          } 
          if(allocated) {
            System.out.println("allocated to woker");
            Gson gson=new GsonBuilder().create();
            String jsonArray=gson.toJson(o.getitemids());
            OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"actor\":\""+chosenActor.getName()+"\",\r\n    \"orderID\":\""+o.getOrderId()+"\",\r\n    \"itemIDs\":"+jsonArray+",\r\n    \"packAtLocation\":\""+getNextAvailableNode()+"\"\r\n}");
            Request request = new Request.Builder()
            .url("http://localhost:8090/handler")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .build();
            Response response = client.newCall(request).execute();
            System.out.println("order sent to worker service");
            chosenActor.setStatus("Working");
            comOrders.add(o);
            //changing status of order to processing
            System.out.println("order status updated");
            RequestBody body2 = RequestBody.create(mediaType,"{\r\n    \"type\": \"Processing\"\r\n}");
            Request request2 = new Request.Builder().url("http://localhost:8083/orders/"+o.getOrderId())
                    .addHeader("Content-Type", "application/json").method("PUT", body2).build();
            try {
                response = client.newCall(request2).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
          }  //logic of assigning workers to orders 
          
          if(!packingNodeAvailable())
           break;
       }
       for(Order in: comOrders ){
           OrderList.orderComplete(in);
       }
    }
   }
    
};