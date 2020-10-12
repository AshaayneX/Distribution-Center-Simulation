package WolfPack.OrderGeneratorService;

import java.util.ArrayList;
import java.util.Random;

public class Inventory{
    //to keep a list of globally available list of orders with the type Item.class objects
    private ArrayList<Item> itemList  = new ArrayList<Item>();
     
   
    private Random rand = new Random(); 
    
    public ArrayList<Item> getRandomList(int size) {
        
        ArrayList<Item> items = new ArrayList<Item>();
       
        if(size>=itemList.size()){
            for(Item i:itemList)
              i.generateQuantity();
              return itemList;
        }
        else{
             
          for(int i=0;i<size;i++){
            int j = rand.nextInt(itemList.size()); 
            Item random = new Item();
            random = itemList.get(j);
            random.generateQuantity();
            items.add(random);
            itemList.remove(j);
          }
          return items;
        } 
    }
    
    public void addItem(Item item) {
        this.itemList.add(item);
    }

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }

    public Inventory() {
    }

   
};