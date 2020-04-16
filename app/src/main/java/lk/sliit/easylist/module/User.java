package lk.sliit.easylist.module;

public class User {
    private String userType;

    public User(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
