package models;

import java.util.List;

public class Cart {
    private final List<LineItem> lineItems;

    public Cart(final List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }
}
