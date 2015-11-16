package models;

public class LineItem {
    private final String name;
    private final int quantity;
    private final String image;

    public LineItem(final String name, final int quantity, final String image) {
        this.name = name;
        this.quantity = quantity;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImage() {
        return image;
    }
}
