import java.util.*;

public class Main {

    public static void main(
            String[] args) {

        FixedClock clock =
                new FixedClock(10);

        Clinic clinic =
                new Clinic();

        Patient patient =
                new Patient(
                        "John",
                        1);

        Doctor doctor =
                new Doctor(
                        "Smith",
                        10);

        Nurse nurse =
                new Nurse(
                        "Jane",
                        20);

        Pharmacist pharmacist =
                new Pharmacist(
                        "Tom",
                        30);

        clinic.registerPerson(patient);
        clinic.registerPerson(doctor);
        clinic.registerPerson(nurse);
        clinic.registerPerson(pharmacist);

        clinic.printFees(
                Arrays.asList(
                        doctor,
                        nurse,
                        pharmacist
                ));

        Appointment ap1 =
                new Appointment(
                        patient,
                        doctor,
                        15);

        clinic.addAppointment(ap1);

        ap1.confirm();

        try(ClinicLogger logger =
                    new ClinicLogger()) {

            logger.log(
                    "Appointment created");

            ap1.cancel(clock);

            logger.log(
                    "Appointment cancelled");
        }

        catch(Exception e) {

            System.out.println(
                    e.getMessage());
        }

        try {

            Patient duplicate =
                    new Patient(
                            "Mike",
                            1);

            clinic.registerPerson(
                    duplicate);

        } catch(Exception e) {

            System.out.println(
                    "EXPECTED FAILURE: "
                            + e.getMessage());
        }

        try {

            new Patient(
                    "",
                    50);

        } catch(Exception e) {

            System.out.println(
                    "EXPECTED FAILURE: "
                            + e.getMessage());
        }

        MedicalLecturer ml =
                new MedicalLecturer(
                        "Brown",
                        100,
                        "OOP");

        System.out.println(
                ml.role());

        System.out.println(
                ml.getModule());
    }
}