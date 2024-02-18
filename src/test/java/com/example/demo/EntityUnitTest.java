package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.entities.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EntityUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    private Doctor doctor;
    private Patient patient;
    private Room room;
    private Appointment appointment;

    @Before
    public void setUp() {
        doctor = new Doctor("John", "Doe", 35, "john.doe@example.com");
        patient = new Patient("Alice", "Smith", 28, "alice.smith@example.com");
        room = new Room("Cardiology");

        appointment = new Appointment(patient, doctor, room,
                LocalDateTime.now(), LocalDateTime.now().plusHours(1));
    }

    @Test
    public void saveAndRetrieveDoctor() {
        Doctor savedDoctor = entityManager.persistAndFlush(doctor);
        Doctor retrievedDoctor = entityManager.find(Doctor.class, savedDoctor.getId());
        assertNotNull(retrievedDoctor);
        assertEquals(savedDoctor.getId(), retrievedDoctor.getId());
        assertEquals(savedDoctor.getFirstName(), retrievedDoctor.getFirstName());
    }

    @Test
    public void saveAndRetrievePatient() {
        Patient savedPatient = entityManager.persistAndFlush(patient);
        Patient retrievedPatient = entityManager.find(Patient.class, savedPatient.getId());
        assertNotNull(retrievedPatient);
        assertEquals(savedPatient.getId(), retrievedPatient.getId());
        assertEquals(savedPatient.getFirstName(), retrievedPatient.getFirstName());
    }

    @Test
    public void saveAndRetrieveRoom() {
        Room savedRoom = entityManager.persistAndFlush(room);
        Room retrievedRoom = entityManager.find(Room.class, savedRoom.getRoomName());
        assertNotNull(retrievedRoom);
        assertEquals(savedRoom.getRoomName(), retrievedRoom.getRoomName());
    }

    @Test
    public void saveAndRetrieveAppointment() {
        Appointment savedAppointment = entityManager.persistAndFlush(appointment);
        Appointment retrievedAppointment = entityManager.find(Appointment.class, savedAppointment.getId());
        assertNotNull(retrievedAppointment);
        assertEquals(savedAppointment.getId(), retrievedAppointment.getId());
        assertEquals(savedAppointment.getPatient(), retrievedAppointment.getPatient());
        assertEquals(savedAppointment.getDoctor(), retrievedAppointment.getDoctor());
        assertEquals(savedAppointment.getRoom(), retrievedAppointment.getRoom());
    }
}