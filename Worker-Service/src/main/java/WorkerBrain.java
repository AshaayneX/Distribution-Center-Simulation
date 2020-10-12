import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import Messages.PerformOrder;
import Messages.PerformSplitOrder;
import Messages.PatchWorker;
import Messages.Die;
import Messages.NotifyWorker;
import akka.actor.AbstractActor;
import okhttp3.*;

public class WorkerBrain extends AbstractActor {
    
    private ArrayList<Action> actions = new ArrayList<Action>();
    private String uri =  "http://localhost:8082/workers/"+ self().path().name();

    private static RestTemplate restTemplate = new RestTemplate();
     
    OkHttpClient client = new OkHttpClient().newBuilder()
    .build();
    MediaType mediaType = MediaType.parse("application/json");
    Response response;

    public Receive createReceive() {
        return receiveBuilder().
            match(PerformOrder.class,
            msg -> {
                System.out.println("recieved normal order");
                String[] items = msg.getItems();
                String location = restTemplate.getForObject(uri+"/location",String.class);
                String nextlocation;
                //moving to the location of the item
                for(String s:items){
                    nextlocation = restTemplate.getForObject("http://localhost:8082/items/location/"+s,String.class);
                    String[] path = restTemplate.getForObject("http://localhost:8082/map/path/"+location+"/"+nextlocation,String[].class);
                    
                    if(path.length>1){
                    for(int i =1;i<path.length;i++){

                         actions.add(new Action("move",path[i]));  
                    }
                  }
                    actions.add(new Action("pick",s));
                    location=nextlocation;
                }
                //moving to the drop location
                String[] path = restTemplate.getForObject("http://localhost:8082/map/path/"+location+"/"+msg.getPackAtLocation(),String[].class);
                if(path.length>1){
                    for(int i =1;i<path.length;i++){
                        actions.add(new Action("move",path[i]));  
                    }
                  } 
                //dropping  
                for(String s:items){
                    actions.add(new Action("drop",s));
                }  
                //marking complete
                actions.add(new Action("pack",msg.getOrderId()));
                actions.add(new Action("node",msg.getPackAtLocation()));
                
            }).
            match(PerformSplitOrder.class,
            msg -> {
                System.out.println("recieved split order");
           
            }).
            match(Die.class,
            msg -> {
                getContext().stop(self());
                System.out.println("Aiyo Maruna");
           
            }).
            match(NotifyWorker.class,
            msg -> {

                if(!actions.isEmpty()){
                System.out.println("performing next step");
                Action a = actions.get(0);
                if(a.getType().equals("pack")){
                  //sending pack order to worker
                  RequestBody body3 = RequestBody.create(mediaType, "{\r\n    \"type\": \""+a.getType()+"\",\r\n    \"arguments\": \""+a.getArguments()+"\"\r\n}");
                    Request request3 = new Request.Builder()
                    .url(uri+"/nextAction")
                    .method("PUT", body3)
                    .addHeader("Content-Type", "application/json")
                    .build();
                     response = client.newCall(request3).execute();
                
                //marking the order as complete
                   RequestBody body = RequestBody.create(mediaType, "{\r\n    \"type\": \"Completed\"\r\n}");
                   Request request = new Request.Builder()
                   .url("http://localhost:8083/orders/"+a.getArguments())
                   .method("PUT", body)
                   .addHeader("Content-Type", "application/json")
                   .build();
                   response = client.newCall(request).execute();

                  //showing availability to the management server
                  RequestBody body2 = RequestBody.create(mediaType, "");
                  Request request2 = new Request.Builder()
                  .url("http://localhost:8086/registry/"+self().path().name())
                  .method("PUT", body2)
                  .build();
                   response = client.newCall(request2).execute();
                }
                else if(a.getType().equals("node")){
                    RequestBody body4 = RequestBody.create(mediaType, "");
                    Request request4 = new Request.Builder()
                    .url("http://localhost:8086/packing/"+a.getArguments())
                    .method("PUT", body4)
                    .build();
                     response = client.newCall(request4).execute();
                }
                else{
                    //setting nextAction of the worker
                    RequestBody body = RequestBody.create(mediaType, "{\r\n    \"type\": \""+a.getType()+"\",\r\n    \"arguments\": \""+a.getArguments()+"\"\r\n}");
                    Request request = new Request.Builder()
                    .url(uri+"/nextAction")
                    .method("PUT", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
                     response = client.newCall(request).execute();
                }
                actions.remove(0);
            }
           else {
            System.out.println("nothing to perform");

           }
            }).
            match(PatchWorker.class,
            msg -> {
                RequestBody body = RequestBody.create(mediaType, "{\r\n    \"notificationUri\" : \""+self().path().toString()+"\"   \r\n}");
                Request request = new Request.Builder()
                .url(uri)
                .method("PATCH", body)
                .addHeader("Content-Type", "application/json")
                .build();
                response = client.newCall(request).execute();
           
            }).build();
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    
}
