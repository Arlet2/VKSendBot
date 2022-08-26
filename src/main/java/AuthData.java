public class AuthData {
    private final String key;
    private final String username;

    AuthData(String key, String username) {
        this.key = key;
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public String getUsername() {
        return username;
    }
}
