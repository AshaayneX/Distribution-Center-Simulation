package Messages;

public class PerformOrder{
    
    private String packAtLocation;
    private String[] items;
    private String orderId;

    public String getPackAtLocation() {
        return packAtLocation;
    }

    public void setPackAtLocation(String packAtLocation) {
        this.packAtLocation = packAtLocation;
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public PerformOrder(String packAtLocation, String[] items, String orderId) {
        this.packAtLocation = packAtLocation;
        this.items = items;
        this.orderId = orderId;
    }

    
}