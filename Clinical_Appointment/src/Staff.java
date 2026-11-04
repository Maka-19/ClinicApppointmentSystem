public abstract class Staff extends Person {

    public Staff(String name, int id) {
        super(name, id);
    }

    public abstract double fee();

    public abstract int bufferHours();
}