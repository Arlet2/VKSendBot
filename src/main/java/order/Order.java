package order;

public class Order {
    private final String msg;
    private final String[] names;

    public Order(String msg, String[] names) {
        this.msg = msg;
        this.names = names;
    }

    public String getMsg() {
        return msg;
    }

    public String[] getNames() {
        return names;
    }
}
