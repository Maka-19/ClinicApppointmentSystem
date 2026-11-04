import java.util.*;

public class Clinic {

    private List<Appointment>
            appointments = new ArrayList<>();

    private Set<Integer> ids =
            new HashSet<>();

    public void registerPerson(Person p) {

        if(ids.contains(p.getId())) {

            throw new IllegalStateException(
                    "Duplicate ID");
        }

        ids.add(p.getId());
    }

    public void addAppointment(
            Appointment appointment) {

        for(Appointment a : appointments) {

            if(a.getStatus() ==
                    AppointmentStatus.CANCELLED)
                continue;

            boolean sameHour =
                    a.getStartHour() ==
                            appointment
                                    .getStartHour();

            if(sameHour &&
                    a.getPatient().getId()
                            ==
                            appointment
                                    .getPatient()
                                    .getId()) {

                throw new IllegalStateException(
                        "Patient conflict");
            }

            if(sameHour &&
                    a.getStaff().getId()
                            ==
                            appointment
                                    .getStaff()
                                    .getId()) {

                throw new IllegalStateException(
                        "Staff conflict");
            }
        }

        appointments.add(appointment);
    }

    public List<Appointment>
    getAppointments() {

        return new ArrayList<>(
                appointments);
    }

    public void printFees(
            List<Staff> staffList) {

        for(Staff s : staffList) {

            System.out.println(
                    s.role()
                            + " Fee = "
                            + s.fee());
        }
    }
}