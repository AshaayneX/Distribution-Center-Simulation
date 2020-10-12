package WolfPack.SimulatorService;

import java.util.ArrayList;

public class Vertex {
    
    private String name;
    private ArrayList <Worker> workersAtLocation = new ArrayList<Worker>();
    private ArrayList <Item> itemsAtLocation  = new ArrayList<Item>();
    private ArrayList <String> shelvesAtLocation  = new ArrayList<String>();
    private boolean isPackagingPoint = false;
    

    public ArrayList<Worker> getWorkersAtLocation() {
        return workersAtLocation;
    }

    public void setWorkersAtLocation(ArrayList<Worker> workersAtLocation) {
        this.workersAtLocation = workersAtLocation;
    }

    public ArrayList<Item> getItemsAtLocation() {
        return itemsAtLocation;
    }

    public void setItemsAtLocation(ArrayList<Item> itemsAtLocation) {
        this.itemsAtLocation = itemsAtLocation;
    }

    public ArrayList<String> getShelvesAtLocation() {
        return shelvesAtLocation;
    }

    public void setShelvesAtLocation(ArrayList<String> shelvesAtLocation) {
        this.shelvesAtLocation = shelvesAtLocation;
    }

    public boolean isPackagingPoint() {
        return isPackagingPoint;
    }

    public void setPackagingPoint(boolean isPackagingPoint) {
        this.isPackagingPoint = isPackagingPoint;
    }

    public Vertex(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addShelf(String name) {
        this.shelvesAtLocation.add(name);
    }
    
    public void addWorker(Worker w) {
        this.workersAtLocation.add(w);
    }

    public void addItem(Item i) {
        this.itemsAtLocation.add(i);
    }

    public void packOrder(){
        for(Item i:itemsAtLocation){
            boolean originalItem =false;
            for(String s:shelvesAtLocation){
                if(i.getLocation().equals(s))
                originalItem=true;
            }
            if(!originalItem)
            itemsAtLocation.remove(i);
        }
    }
   
}
