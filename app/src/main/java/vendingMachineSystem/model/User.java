package vendingMachineSystem.model;

public class User {
    /*
    THIS IS ONLY IMPLEMENTED TO THE EXTENT FOR GENERATING A USER REPORT
     */

    private String username;
    private String role;

    public String getUsername() {
        return username;
    }
    public String getRole(){
        return role;
    }

    public User(String username, String role){
        this.role = role;
        this.username = username;
    }
}
