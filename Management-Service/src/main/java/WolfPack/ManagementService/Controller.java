package WolfPack.ManagementService;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import okhttp3.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class Controller {

  // getting new order id's
  @RequestMapping(value = "/newOrder", method = RequestMethod.POST)
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void registerService(@RequestBody NewOrder o) throws Exception {
    System.out.print("got new order : " + o.getNewOrder());
    o.addOrderToQueue();
    Processing.processOrder();
  }

  // register actors from worker service
  @RequestMapping(value = "/registry", method = RequestMethod.POST)
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void registerWorkers(@RequestBody Actor a) {
    System.out.println("Actor :" + a.getName());
    // a.setAttributes();
    ActorList.addToList(a);
  }

  @RequestMapping(value = "/registry/{name}", method = RequestMethod.PUT)
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void registerWorkers(@PathVariable("name") String name) throws Exception {
    ActorList.getActorbyName(name).setStatus("Available");
  }

  @RequestMapping(value = "/start", method = RequestMethod.POST)
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void start() {
    System.out.println("Management Started");
    ActorList.setActorsAttributes();
    Processing.registerForOrders();
  }

  @RequestMapping(value = "/process", method = RequestMethod.POST)
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void process() throws Exception {
    System.out.println("Process Started");

  }

  @RequestMapping(value = "/packing/{node}", method = RequestMethod.POST)
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void process(@PathVariable("node") String key) {
    Processing.makeNodeAvailable(key);
  }

  @RequestMapping(value = "/reset", method = RequestMethod.POST)
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void reset() throws IOException {
      OkHttpClient client = new OkHttpClient().newBuilder()
      .build();
      MediaType mediaType = MediaType.parse("text/plain");
      okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "");
      Request request = new Request.Builder()
      .url("http://localhost:8083/reset")
      .method("POST",body)
      .build();
      Response response = client.newCall(request).execute();

      ActorList.reset();
      OrderList.reset();

      MediaType mediaType2 = MediaType.parse("application/json");
      okhttp3.RequestBody body2 = okhttp3.RequestBody.create(mediaType2, "{\r\n    \"type\" : \"reset\"\r\n}");
      Request request2 = new Request.Builder()
      .url("http://localhost:8090/reset")
      .method("POST", body2)
      .addHeader("Content-Type", "application/json")
      .build();
      response = client.newCall(request2).execute(); 
    }
}