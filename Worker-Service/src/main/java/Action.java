

public class Action {
    
        private String type;
        private String arguments;
    
        public String getType() {
            return type;
        }
    
        public void setType(String type) {
            this.type = type;
        }
    
        public String getArguments() {
            return arguments;
        }
    
        public void setArguments(String arguments) {
            this.arguments = arguments;
        }

    public Action(String type, String arguments) {
        this.type = type;
        this.arguments = arguments;
    }
    
     
    
}
