package WolfPack.SimulatorService;

import java.util.ArrayList;

public class Config {

   private  String aisles;
   private  String sections;
   private  String shelves;
   private  String packagingAreas[];
   private  ArrayList <Worker> workers;
   private  ArrayList <Item> items ;

    public  String getAisles() {
        return aisles;
    }

    public  String getSections() {
        return sections;
    }

    public  String getShelves() {
        return shelves;
    }

    public  String[] getPackagingAreas() {
        return packagingAreas;
    }

    public  ArrayList<Worker> getWorkers() {
        return workers;
    }

    public  ArrayList<Item> getItems() {
        return items;
    }  

};