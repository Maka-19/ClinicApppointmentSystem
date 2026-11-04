/*
INVARIANTS

patient != null
staff != null
0 <= startHour <= 168
status != null
*/

public class Appointment {

    private Patient patient;
    private Staff staff;
    private int startHour;

    private AppointmentStatus status;

    public Appointment(
            Patient patient,
            Staff staff,
            int startHour) {

        if(startHour < 0 || startHour > 168)
            throw new IllegalArgumentException(
                    "Hour out of range");

        this.patient = patient;
        this.staff = staff;
        this.startHour = startHour;

        this.status =
                AppointmentStatus.PENDING;
    }

    public Patient getPatient() {
        return patient;
    }

    public Staff getStaff() {
        return staff;
    }

    public int getStartHour() {
        return startHour;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void confirm() {

        if(status != AppointmentStatus.PENDING)
            throw new IllegalStateException(
                    "Only pending can confirm");

        status =
                AppointmentStatus.CONFIRMED;
    }

    public void cancel(Clock clock) {

        if(status ==
                AppointmentStatus.CANCELLED) {

            throw new IllegalStateException(
                    "Already cancelled");
        }

        if(clock.now() >= startHour)
            throw new IllegalStateException(
                    "Already started");

        if(status ==
                AppointmentStatus.CONFIRMED
                &&
                startHour - clock.now() < 2) {

            throw new IllegalStateException(
                    "Too late");
        }

        status =
                AppointmentStatus.CANCELLED;
    }
}