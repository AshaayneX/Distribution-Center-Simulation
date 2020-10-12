package WolfPack.OrderGeneratorService;

public class Subscriber{
    private String uri = "";

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Subscriber() {
    }
   
    public boolean hasSubscriber(){
        boolean b = true;
        if(this.uri.equals(""))
          b=false;
        return b;  
    }
  
};