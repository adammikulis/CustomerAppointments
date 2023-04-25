package helper;

public class SessionManager {
    private static SessionManager instance;
    private String currentUserName;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String userName) {
        this.currentUserName = userName;
    }
}
