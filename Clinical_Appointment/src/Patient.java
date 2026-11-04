public class Patient extends Person {

    public Patient(String name, int id) {
        super(name, id);
    }

    @Override
    public String role() {
        return "Patient";
    }
}