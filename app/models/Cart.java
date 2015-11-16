package models;

import java.util.List;

public class Cart {
    private List<LineItem> lineItems;

    public Cart() {
    }

    public Cart(final List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(final List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }
}
