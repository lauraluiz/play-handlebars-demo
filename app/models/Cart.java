package models;

import java.util.List;

public class Cart {
    private List<LineItem> lineItems;
    private int totalItems;

    public Cart() {
    }

    public Cart(final List<LineItem> lineItems) {
        this.lineItems = lineItems;
        this.totalItems = lineItems.stream().mapToInt(item -> item.getQuantity()).sum();
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(final List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(final int totalItems) {
        this.totalItems = totalItems;
    }
}
