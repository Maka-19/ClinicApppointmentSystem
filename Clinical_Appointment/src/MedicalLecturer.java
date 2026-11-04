public class MedicalLecturer
        extends Doctor
        implements Lecturer {

    private String module;

    public MedicalLecturer(
            String name,
            int id,
            String module) {

        super(name, id);

        this.module = module;
    }

    @Override
    public String role() {
        return "Doctor and Lecturer";
    }

    @Override
    public String getModule() {
        return module;
    }
}