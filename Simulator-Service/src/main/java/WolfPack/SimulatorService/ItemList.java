package WolfPack.SimulatorService;

import java.util.ArrayList;

public class ItemList{
    //to keep a list of globally available list of orders with the type Item.class objects
    private static ArrayList<Item> itemList;

    public static ArrayList<Item> getItemList() {
        return itemList;
    }

    public static void setItemList(ArrayList<Item> itemList) {
        ItemList.itemList = itemList;
    }
    
    public static Item getItemById(String id) throws NoSuchItem {
        ArrayList<Item> temp = ItemList.getItemList();
        for(Item x:temp){
             if(x.getId().equals(id))
              return x;
         }
         throw new NoSuchItem();
    }

    public static Item getItemInShelf(String id) throws NoSuchItem {
        
        for(Item x:itemList){
             if(x.getLocation().equals(id))
              return x;
         }
         throw new NoSuchItem();
    }
};
