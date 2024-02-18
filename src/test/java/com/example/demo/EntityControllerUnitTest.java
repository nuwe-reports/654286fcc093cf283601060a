
package com.example.demo;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import com.example.demo.controllers.*;
import com.example.demo.repositories.*;
import com.example.demo.entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;



@WebMvcTest(DoctorController.class)
class DoctorControllerUnitTest{

    @MockBean
    private DoctorRepository doctorRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getDoctorById() throws Exception {
        Doctor doctor1 = new Doctor("Profesor", "Layton", 40, "mail@mail.com");

        doctorRepository.save(doctor1);

        when(doctorRepository.findById(doctor1.getId())).thenReturn(Optional.of(doctor1));

        mockMvc.perform(get("/api/doctors/{id}", doctor1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(doctor1.getId()))
                .andExpect(jsonPath("$.firstName").value(doctor1.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(doctor1.getLastName()))
                .andExpect(jsonPath("$.age").value(doctor1.getAge()))
                .andExpect(jsonPath("$.email").value(doctor1.getEmail()));
    }
    @Test
    void getAllDoctors() throws Exception {
        Doctor doctor1 = new Doctor("John", "Doe", 30, "john.doe@mail.com");
        Doctor doctor2 = new Doctor("Jane", "Smith", 35, "jane.smith@mail.com");
        List<Doctor> doctors = Arrays.asList(doctor1, doctor2);

        when(doctorRepository.findAll()).thenReturn(doctors);

        mockMvc.perform(get("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].id").value(doctor1.getId()))
                .andExpect(jsonPath("$[0].firstName").value(doctor1.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(doctor1.getLastName()))
                .andExpect(jsonPath("$[0].age").value(doctor1.getAge()))
                .andExpect(jsonPath("$[0].email").value(doctor1.getEmail()))

                // Doctor 2 assertions
                .andExpect(jsonPath("$[1].id").value(doctor2.getId()))
                .andExpect(jsonPath("$[1].firstName").value(doctor2.getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(doctor2.getLastName()))
                .andExpect(jsonPath("$[1].age").value(doctor2.getAge()))
                .andExpect(jsonPath("$[1].email").value(doctor2.getEmail()));
    }

    @Test
    void getAllDoctorsEmpty() throws Exception {
        when(doctorRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    @Test
    void postDoctor() throws Exception {
        mockMvc.perform(post("/api/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Ash\",\"lastName\":\"Ketchup\",\"age\":16,\"email\":\"mail@mail.com\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
    @Test
    void deleteDoctor() throws Exception {
        // Create a doctor for testing
        mockMvc.perform(post("/api/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Ash\",\"lastName\":\"Ketchup\",\"age\":16,\"email\":\"mail@mail.com\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/api/doctor/0"))
                .andExpect(status().isNotFound());
    }
    @Test
    void deleteAllDoctors() throws Exception {
        mockMvc.perform(delete("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(doctorRepository).deleteAll();
    }
}


@WebMvcTest(PatientController.class)
class PatientControllerUnitTest{

    @MockBean
    private PatientRepository patientRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void getPatientById() throws Exception {
        Patient patient = new Patient("Pedro", "Gonzalez", 20, "mail@mail.com");

        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));

        mockMvc.perform(get("/api/patients/{id}", patient.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patient.getId()))
                .andExpect(jsonPath("$.firstName").value(patient.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(patient.getLastName()))
                .andExpect(jsonPath("$.age").value(patient.getAge()))
                .andExpect(jsonPath("$.email").value(patient.getEmail()));
    }
    @Test
    void getAllPatients() throws Exception {
        Patient patient1 = new Patient("John", "Doe", 30, "john.doe@mail.com");
        Patient patient2 = new Patient("Jane", "Smith", 35, "jane.smith@mail.com");
        List<Patient> patients = Arrays.asList(patient1, patient2);

        when(patientRepository.findAll()).thenReturn(patients);

        mockMvc.perform(get("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].id").value(patient1.getId()))
                .andExpect(jsonPath("$[0].firstName").value(patient1.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(patient1.getLastName()))
                .andExpect(jsonPath("$[0].age").value(patient1.getAge()))
                .andExpect(jsonPath("$[0].email").value(patient1.getEmail()))

                .andExpect(jsonPath("$[1].id").value(patient2.getId()))
                .andExpect(jsonPath("$[1].firstName").value(patient2.getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(patient2.getLastName()))
                .andExpect(jsonPath("$[1].age").value(patient2.getAge()))
                .andExpect(jsonPath("$[1].email").value(patient2.getEmail()));
    }

    @Test
    void getAllPatientsEmpty() throws Exception {
        when(patientRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    @Test
    void postPatient() throws Exception {
        mockMvc.perform(post("/api/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Ash\",\"lastName\":\"Ketchup\",\"age\":16,\"email\":\"mail@mail.com\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
    @Test
    void deletePatient() throws Exception {
        // Create a doctor for testing
        mockMvc.perform(post("/api/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Ash\",\"lastName\":\"Ketchup\",\"age\":16,\"email\":\"mail@mail.com\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/api/patients/0"))
                .andExpect(status().isNotFound());
    }
    @Test
    void deleteAllPatients() throws Exception {
        mockMvc.perform(delete("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(patientRepository).deleteAll();
    }
}

@WebMvcTest(RoomController.class)
class RoomControllerUnitTest{

    @MockBean
    private RoomRepository roomRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllRooms() throws Exception {
        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room("Cardiology"));

        when(roomRepository.findAll()).thenReturn(rooms);

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomName").value("Cardiology"));

        verify(roomRepository, times(1)).findAll();
    }

    @Test
    void getRoomByRoomName() throws Exception {
        Room room = new Room("Cardiology");

        when(roomRepository.findByRoomName("Cardiology")).thenReturn(Optional.of(room));

        mockMvc.perform(get("/api/rooms/Cardiology"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomName").value("Cardiology"));


        verify(roomRepository, times(1)).findByRoomName("Cardiology");
    }

    @Test
    void createRoom() throws Exception {
        Room room = new Room("Cardiology");

        mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomName\":\"Cardiology\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roomName").value("Cardiology"));

        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void deleteRoom() throws Exception {
        when(roomRepository.findByRoomName("Cardiology")).thenReturn(Optional.of(new Room("Cardiology")));

        mockMvc.perform(delete("/api/rooms/Cardiology"))
                .andExpect(status().isOk());

        verify(roomRepository, times(1)).deleteByRoomName("Cardiology");
    }

    @Test
    void deleteAllRooms() throws Exception {
        mockMvc.perform(delete("/api/rooms"))
                .andExpect(status().isOk());

        verify(roomRepository, times(1)).deleteAll();
    }

}