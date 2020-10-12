package WolfPack.SimulatorService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.tools.DocumentationTool.Location;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")

public class Controller {

  private Config c;
  public Step s;
  private Layout l = new Layout();

  @RequestMapping(value = "/configuration", method = RequestMethod.PUT)
  @ResponseStatus(value = HttpStatus.OK)
  public void configure(@RequestBody Config config) throws IOException {
    c = config;
    WorkerList.setWorkerList(config.getWorkers());
    ItemList.setItemList(config.getItems());
    Map.setAisles(config.getAisles());
    Map.setPackagingAreas(config.getPackagingAreas());
    Map.setSections(config.getSections());
    Map.setShelves(config.getShelves());
    Map.createMap();
  }

  @RequestMapping(value = "/configuration", method = RequestMethod.GET)
  public Config getCurrentConfig() {
    return c;
  }
  @RequestMapping(value = "/items", method = RequestMethod.GET)
  public ArrayList<Item> getItems() {
    return ItemList.getItemList();
  }

  @RequestMapping(value = "/items/{Id}", method = RequestMethod.GET)
  public Item getItemById(@PathVariable("Id") String Id) throws NoSuchItem {
    return ItemList.getItemById(Id);
  }

  @RequestMapping(value = "/items/location/{Id}", method = RequestMethod.GET)
  public String getItemVertexLocation(@PathVariable("Id") String Id) throws NoSuchItem {
    String split[] = ItemList.getItemById(Id).getLocation().split("/");
    return split[0];

  }

  @RequestMapping(value = "/items/{Id}/{location}/{sub}", method = RequestMethod.POST)
  public void setLocation(@PathVariable("Id") String Id,@PathVariable("location") String loc ,@PathVariable("sub") String sub) throws NoSuchItem {
    String subs= loc+"/"+sub;
    ItemList.getItemById(Id).setLocation(subs);
  }

  @RequestMapping(value = "/workers", method = RequestMethod.GET)
  public String[] getWorkers() {
    return WorkerList.getWorkerNames();
  }

  @RequestMapping(value = "/workerlist", method = RequestMethod.GET)
  public ArrayList<Worker> getWorkerList() {
    return WorkerList.getWorkerList();
  }

  @RequestMapping(value = "/workers/{name}", method = RequestMethod.PATCH)
  @ResponseStatus(value = HttpStatus.OK)
  public void setWorkerUri(@PathVariable("name") String name, @RequestBody Notification n) throws NoSuchWorker {
    Worker w = WorkerList.getWorkerByName(name);
    w.setNotificationUri(n.getNotificationUri());
  }

  @RequestMapping(value = "/workers/{name}", method = RequestMethod.GET)
  public Worker getWorkerDetails(@PathVariable("name") String name) throws NoSuchWorker {
    return WorkerList.getWorkerByName(name);
  }

  @RequestMapping(value = "/workers/capacity/{name}", method = RequestMethod.GET)
  public String getWorkerCapacity(@PathVariable("name") String name) throws NoSuchWorker {
    return WorkerList.getWorkerByName(name).getCapacity();
  }

  @RequestMapping(value = "/workers/{name}/location", method = RequestMethod.GET)
  public String getWorkerLocation(@PathVariable("name") String name) throws NoSuchWorker {
    return WorkerList.getWorkerByName(name).getLocation();
  }

  @RequestMapping(value = "/workers/{name}/nextAction", method = RequestMethod.PUT)
  @ResponseStatus(value = HttpStatus.OK)
  public void setNextAction(@PathVariable("name") String name, @RequestBody Action a) throws NoSuchWorker {
    Worker w = WorkerList.getWorkerByName(name);
    w.setNextAction(a);
  }

  @RequestMapping(value = "/step", method = RequestMethod.PUT)
  @ResponseStatus(value = HttpStatus.OK)
  public void setCurrentStep(@RequestBody Step step) throws NoSuchItem, NoSuchVertex {
    s = step;
    if(WorkerList.getWorkerList()!= null){
      Map.updateMap();
      WorkerList.process();
    }
  }

  @RequestMapping(value = "/step", method = RequestMethod.GET)
  public Step getCurrentStep() {
    return s;
  }

  @RequestMapping(value = "/map", method = RequestMethod.GET)
  public Layout getCurrentMap() {

    l.vertices = Map.floorPlan.vertexSet();
    for (DefaultEdge s : Map.floorPlan.edgeSet()) {
      l.edges.add(s.toString());
    }

    return l;
  }
  
  @RequestMapping(value = "/map/vertexlist", method = RequestMethod.GET)
  public ArrayList<Vertex> getVertices() {

    return VertexList.getVertexList();
  }

  @RequestMapping(value = "/map/vertices", method = RequestMethod.GET)
  public Set<String> getMapVertices() {

    return Map.floorPlan.vertexSet();
  }

  @RequestMapping(value = "/map/vertices/{id}", method = RequestMethod.GET)
  public Vertex getVerticeDetails(@PathVariable("id") String id) throws NoSuchVertex {

    return VertexList.getVertexByName(id);
  }

  @RequestMapping(value = "/map/vertices/{id}/shelf", method = RequestMethod.GET)
  public ArrayList<String> getShelfs(@PathVariable("id") String id) throws NoSuchVertex {

    return VertexList.getVertexByName(id).getShelvesAtLocation();
  }

  @RequestMapping(value = "/map/vertices/{id}/opposite", method = RequestMethod.GET)
  public ArrayList<String> getOppositeVertice(@PathVariable("id") String id) throws NoSuchVertex {
    Vertex v = VertexList.getVertexByName(id);
    ArrayList<String> response = new ArrayList<String>();
    Set<DefaultEdge> edgesOf = Map.floorPlan.edgesOf(v.getName());
    for (DefaultEdge e : edgesOf) {
      String t = Map.floorPlan.getEdgeTarget(e).toString();
      if (!t.equals(v.getName())) {
        response.add(t);
      }
      t = Map.floorPlan.getEdgeSource(e).toString();
      if (!t.equals(v.getName())) {
        response.add(t);
      }
    }
    return response;
  }

  @RequestMapping(value = "/map/packaging", method = RequestMethod.GET)
  public String[] getPackingPoints() {
   
    return c.getPackagingAreas();
}

@RequestMapping(value = "/map/{source}/{target}", method = RequestMethod.GET)
public String getShortestPathLength(@PathVariable("source") String source, @PathVariable("target") String target) {
  GraphPath<String, DefaultEdge> shortestPath = DijkstraShortestPath.findPathBetween(Map.floorPlan, source, target);
  String  i = String.valueOf(shortestPath.getLength());
  return i;
}

@RequestMapping(value = "/map/path/{source}/{target}", method = RequestMethod.GET)
public List<String> getPath(@PathVariable("source") String source, @PathVariable("target") String target) {
  GraphPath<String, DefaultEdge> shortestPath = DijkstraShortestPath.findPathBetween(Map.floorPlan, source, target);
  
  return shortestPath.getVertexList();
}

};