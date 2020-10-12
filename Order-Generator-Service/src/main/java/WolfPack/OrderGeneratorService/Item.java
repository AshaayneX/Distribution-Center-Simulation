package WolfPack.OrderGeneratorService;

import java.util.Random;

public class Item {
    
    private String   id ;
    private String   weight;  
    private String   location ;
    private int quantity = 1;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getQuantity() {
        return quantity;
    }
    
    public void generateQuantity(){
        Random rand = new Random();
        int i = rand.nextInt(100);
        if(i<3){
            this.quantity = 3;
        }
        else if(i<33){
            this.quantity = 2;    
        }
        else{
            this.quantity = 1;
        }
    }

    public Item() {
    }
};
