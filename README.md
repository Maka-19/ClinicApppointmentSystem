
# Clinic Appointment Management System
# MAKANAKA M MASIIWA :R2420425
# IRVINE MANGUDHLA :R2420384

## 1. Project Overview

The Clinic Appointment Management System is a Java console-based
application for managing patients, medical staff and clinic
appointments. It supports appointment booking, confirmation and
cancellation, consultation fees, patient discounts, waiting lists,
emergency bookings, staff scheduling buffers and activity logging.

The system is designed to demonstrate object-oriented programming
concepts, including abstraction, encapsulation, inheritance,
polymorphism, interfaces and composition/association.


## 3. Main Features

-   Register patients and staff members.
-   Book, confirm and cancel appointments.
-   Enforce appointment status and cancellation rules.
-   Prevent duplicate IDs across patients and staff.
-   Prevent a patient or staff member from having conflicting
    appointments.
-   Calculate consultation fees using polymorphism.
-   Apply patient discounts through a discount policy interface.
-   Maintain staff waiting lists and offer cancelled slots to waiting
    patients.
-   Support emergency bookings that can replace eligible pending
    appointments.
-   Apply staff-specific scheduling buffers.
-   Record clinic actions and failures in a log.
-   Use an injectable `Clock` so tests do not depend on the computer's
    actual time.

## 4. UML Class Diagram


``` mermaid
classDiagram
    class Person {
        <<abstract>>
        -String name
        -int id
        +role() String
    }

    class Patient {
        -boolean emergency
        -DiscountPolicy discountPolicy
        +role() String
    }

    class Staff {
        <<abstract>>
        +fee(int patientId) double
        +bufferHours() int
    }

    class Doctor {
        +fee(int patientId) double
        +bufferHours() int
    }

    class Nurse {
        +fee(int patientId) double
        +bufferHours() int
    }

    class Pharmacist {
        +fee(int patientId) double
        +bufferHours() int
    }

    class Lecturer {
        <<interface>>
        +moduleName() String
    }

    class MedicalLecturer {
        +role() String
        +fee(int patientId) double
    }

    class Appointment {
        -int appointmentId
        -int startHour
        -AppointmentStatus status
        +confirm() void
        +cancel(Clock clock) void
    }

    class AppointmentStatus {
        <<enumeration>>
        PENDING
        CONFIRMED
        CANCELLED
    }

    class Clinic {
        -List~Patient~ patients
        -List~Staff~ staff
        -List~Appointment~ appointments
        -Clock clock
        -AppointmentLogger logger
        +bookAppointment() Appointment
        +confirmAppointment() void
        +cancelAppointment() void
        +joinWaitingList() void
    }

    class Clock {
        <<interface>>
        +now() int
    }

    class ManualClock {
        -int currentHour
        +now() int
        +setHour(int hour) void
    }

    class AppointmentLogger {
        +log(String message) void
        +close() void
    }

    class DiscountPolicy {
        <<interface>>
        +discount() double
    }

    class StudentDiscount
    class StaffMemberDiscount
    class VisitorDiscount

    Person <|-- Patient
    Person <|-- Staff
    Staff <|-- Doctor
    Staff <|-- Nurse
    Staff <|-- Pharmacist
    Doctor <|-- MedicalLecturer
    Lecturer <|.. MedicalLecturer

    Patient --> DiscountPolicy : uses
    DiscountPolicy <|.. StudentDiscount
    DiscountPolicy <|.. StaffMemberDiscount
    DiscountPolicy <|.. VisitorDiscount

    Appointment --> Patient : booked for
    Appointment --> Staff : assigned to
    Appointment --> AppointmentStatus : has status

    Clinic o-- Patient : manages
    Clinic o-- Staff : manages
    Clinic o-- Appointment : manages
    Clinic --> Clock : obtains current hour from
    Clinic --> AppointmentLogger : records actions

    Clock <|.. ManualClock
    Staff o-- Patient : waiting list
```

## 5. Object-Oriented Design

### 5.1 Abstraction

`Person` and `Staff` are abstract classes. They define common properties
and behaviour without allowing incomplete base objects to be created.
`Person` requires subclasses to provide a role, while `Staff` requires
staff subclasses to provide a fee and scheduling buffer.

### 5.2 Encapsulation

Fields are kept private and are accessed through controlled methods. The
clinic does not expose its internal patient, staff or appointment
collections directly. This prevents external code from changing the
clinic's records without going through the clinic's validation rules.

### 5.3 Inheritance

`Patient` and `Staff` inherit from `Person`. `Doctor`, `Nurse` and
`Pharmacist` inherit from `Staff`. `MedicalLecturer` inherits from
`Doctor`, so it remains a staff member and a person while also
implementing the lecturer interface.

### 5.4 Polymorphism

The clinic can process staff through the common `Staff` type. It calls
`fee()` and `bufferHours()` on each staff object. The object's actual
class determines the implementation. Adding another staff subclass
therefore does not require changing the fee-printing loop.

### 5.5 Interfaces

`Clock`, `Lecturer` and `DiscountPolicy` are interfaces. They define
capabilities that can be implemented by different classes. `ManualClock`
supplies controllable time for testing. Discount classes supply
different discount rates without requiring the booking logic to identify
patient types using type checks.

## 6. Appointment Rules and Invariants

-   Appointment times are represented as integer hours from 0 to 168.
-   Each appointment lasts exactly one hour.
-   The application does not read the computer's system clock. It
    obtains the current hour from a `Clock` object.
-   A new appointment begins in the `PENDING` state.
-   A pending appointment may be confirmed or cancelled.
-   A confirmed appointment cannot be cancelled less than two hours
    before its start.
-   An appointment cannot be cancelled once it has started.
-   A cancelled appointment cannot be confirmed or returned to another
    state.
-   Names must not be empty, and person IDs must be positive.
-   IDs must be unique across the clinic's patients and staff.
-   A patient or staff member cannot have two non-cancelled appointments
    at the same hour.
-   Staff scheduling buffers are also checked when a staff member is
    assigned another appointment.

These rules are enforced by the relevant domain classes and clinic
operations rather than being left to the user to remember.

## 7. Consultation Fees and Discounts

The staff fee calculations are:

  Staff role   Fee calculation
  ------------ ----------------------
  Doctor       `40 + 5 * (P mod 5)`
  Nurse        `10 + (P mod 3)`
  Pharmacist   `5`

Here, `P` is the patient ID. The clinic uses a single loop over `Staff`
objects and invokes the fee method polymorphically. It does not need
`instanceof` checks or a list of staff types.

The discount policy is selected through the `DiscountPolicy` interface:

  Patient category                          Discount
  ------------------ -------------------------------
  Student                                        50%
  Staff member                                    0%
  Visitor              100% of the fee (no discount)

The final amount is calculated using the selected policy and the staff
member's fee.

## 8. Scheduling Variations Implemented

This version includes all four variations for demonstration:

### 8.1 Waiting list

A patient can join a staff member's waiting list. When an appointment is
cancelled, the clinic checks the waiting list and offers the released
slot to the first waiting patient who has no scheduling conflict. The
offered appointment is created as `PENDING` and the action is logged.

### 8.2 Emergency booking

An emergency patient may take a slot occupied by a `PENDING` appointment
at the same hour. The displaced appointment is cancelled with the reason
`bumped`. A `CONFIRMED` appointment is never bumped.

### 8.3 Staff scheduling buffers

The required buffer after an appointment depends on the staff member:

  Staff role      Buffer
  ------------ ---------
  Doctor         2 hours
  Nurse           1 hour
  Pharmacist     0 hours

The clinic asks the staff object for its buffer through `bufferHours()`.
It does not use role-based type checks.

### 8.4 Discounts

The system supports student, staff-member and visitor discount policies.
The patient holds a policy object, and the clinic calculates the amount
through that policy rather than checking the patient's category with
type tests.

**Assignment note:** The assignment describes the variation as being
selected using `P mod 4`. This implementation includes all four
variations because all were requested for the project demonstration. If
the lecturer requires only the one variation assigned to your group,
retain the relevant variation and explain that choice.

## 9. Design Decisions (D1--D6)

### D1. Where should the two-hour cancellation rule be enforced?

The cancellation rule belongs in the `Appointment` class because it is
an appointment invariant. The appointment has the start hour and status
needed to decide whether cancellation is legal. Keeping the rule there
ensures that every caller follows the same restriction. The `Clinic`
coordinates the operation and logs the result, but it should not
duplicate the rule.

### D2. Why use a Clock object?

The application uses a `Clock` interface instead of reading the
computer's clock directly. This makes time controllable during tests and
demonstrations. `ManualClock` can be set to a chosen hour, allowing the
two-hour cancellation boundary and the "appointment has started" rule to
be tested consistently. It also follows the assignment requirement that
the program must ask a Clock object for the current hour.

### D3. What changes when Pharmacist is added?

`Pharmacist` is added as another subclass of `Staff`. It implements its
fee calculation and buffer. The clinic's fee-printing loop and general
booking logic continue to work through the `Staff` abstraction. This
demonstrates extensibility: adding a staff role does not require editing
the loop to add a special case.

### D4. What should MedicalLecturer.fee() return, and how is duplicate Person construction avoided?

`MedicalLecturer` inherits from `Doctor`, so it uses the doctor's fee
calculation. This is appropriate because the object is still a doctor
providing a clinical service; being a lecturer does not create a
separate consultation-fee category in the requirements. `Lecturer` is
represented as an interface in Java. `MedicalLecturer` extends `Doctor`
and implements `Lecturer`, so there is only one class inheritance path
to `Person` and the `Person` constructor runs once.

### D5. How are log opening, writing and closing handled?

The logger is used with Java's try-with-resources mechanism. This
ensures that the logger's `close()` method is invoked when the managed
block finishes, including when an operation throws an exception. The
logger records opening and closing messages and records failed clinic
actions. If writing or closing fails, the I/O failure is surfaced rather
than silently treated as a successful log operation.

### D6. How are the scheduling variations designed?

The variations are implemented through the relevant objects and clinic
operations:

-   Waiting-list entries are associated with staff, and the clinic
    checks patient availability before offering a released slot.
-   Emergency booking may replace only a pending appointment at the same
    hour; confirmed appointments are protected.
-   Staff buffer requirements are obtained by calling `bufferHours()` on
    the staff object, avoiding type checks.
-   Patient discounts are represented by `DiscountPolicy`
    implementations, avoiding category checks in the fee calculation.

This design keeps variation-specific behaviour separate and uses
interfaces and polymorphism where different rules must be selected.

## 10. Logging

The clinic records successful actions and failed actions. Example
messages include:

``` text
[log] opened
BOOK patient #1 with Doctor #10 at hour 40
CONFIRMED appointment #1
FAILED cancel #3: too late
[log] closed
```

The exact messages depend on the operations performed during the
demonstration. The important requirements are that actions and failures
are recorded and that the logger is closed even if an operation fails.

## 11. Running the Project

Open a terminal in the project root directory.

Compile the source files:

``` bash
javac -d out src/clinic/*.java
```

Run the demonstration:

``` bash
java -cp out clinic.Main
```

Run the self test (if the test source is included):

``` bash
javac -cp out -d out tests/clinic/SelfTest.java
java -cp out clinic.SelfTest
```

Use JDK 17 or later.

## 12. Demonstration Plan

The console demonstration should show:

1.  Creating patients and staff members.
2.  Printing staff roles and fees using one polymorphic loop.
3.  Booking and confirming appointments.
4.  Rejecting invalid bookings and duplicate IDs.
5.  Enforcing appointment cancellation rules using a manually controlled
    clock.
6.  Demonstrating waiting-list offers after cancellation.
7.  Demonstrating an emergency booking that bumps a pending appointment
    but not a confirmed one.
8.  Demonstrating staff scheduling buffers.
9.  Demonstrating patient discounts.
10. Demonstrating the `MedicalLecturer` role and confirming that the
    `Person` constructor runs once.
11. Showing successful and failed actions in the log, including logger
    closure.

## 13. Limitations and Possible Improvements

This is a console based academic system. It does not include a
graphical user interface, database persistence, user authentication or
deployment to a production clinic environment. These could be added in a
future version, but are outside the core scope of the assignment.
