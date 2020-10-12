package WolfPack.ManagementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.client.RestTemplate;

public class Actor{
    private String name;
    private String status;
    private String capacity;
    
    @LoadBalanced
    private RestTemplate restTemplate = new RestTemplate();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public Actor(String name) {
        this.name = name;
    }
     
    public void setAttributes(){
    this.status = "Available";
    this.capacity = restTemplate.getForObject("http://localhost:8082/workers/capacity/"+name, String.class);
    }

    public Actor() {
    }
}