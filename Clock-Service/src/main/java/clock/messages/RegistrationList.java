package clock.messages;

import java.util.List;

public class RegistrationList {
    private List<Registration> registrations;

    public RegistrationList(List<Registration> registrations) {
        this.registrations = registrations;
    }

    public RegistrationList() {}
    
    public List<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<Registration> registrations) {
        this.registrations = registrations;
    }
}