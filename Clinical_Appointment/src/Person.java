public abstract class Person {

    private String name;
    private int id;

    public Person(String name, int id) {

        if(name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Name cannot be empty");
        }

        if(id <= 0) {
            throw new IllegalArgumentException(
                    "ID must be positive");
        }

        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public abstract String role();
}