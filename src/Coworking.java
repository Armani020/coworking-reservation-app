public class Coworking {
    private int id;
    private String name;
    private String type;
    private int price;
    private boolean available;

    public Coworking(int id, String name, String type, int price, boolean available) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
