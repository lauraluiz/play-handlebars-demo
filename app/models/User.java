package models;

public class User {
    private String name;

    public User() {
    }

    public User(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
