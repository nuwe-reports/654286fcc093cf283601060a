package com.example.demo.controllers;

import com.example.demo.repositories.AppointmentRepository;
import com.example.demo.entities.Appointment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        appointmentRepository.findAll().forEach(appointments::add);

        if (appointments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable("id") long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);

        if (appointment.isPresent()) {
            return new ResponseEntity<>(appointment.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/appointment")
    public ResponseEntity<List<Appointment>> createAppointment(@RequestBody Appointment appointment){
        // Obtenemos todas las citas
        List<Appointment> existingAppointments = appointmentRepository.findAll();

        //Verificamos que la fecha de comienzo != fecha de finalización
        if (appointment.getStartsAt().isEqual(appointment.getFinishesAt())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // Iteramos sobre todas las citas y usamos appointment.overlaps para verificar que dicha cita no exista
        // Si existe retornamos 406, caso contrario lo guardamos.
        for (Appointment existingAppointment : existingAppointments) {
            if (appointment.overlaps(existingAppointment)) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }
        appointmentRepository.save(appointment);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private boolean hasRoomConflict(Appointment newAppointment) {
        List<Appointment> existingAppointments = appointmentRepository.findAll();
        for (Appointment existingAppointment : existingAppointments) {
            if (existingAppointment.getRoom().equals(newAppointment.getRoom())) {
                return true; // Hay conflicto de habitaciones
            }
        }
        return false; // No hay conflicto de habitaciones
    }
    private boolean isValidAppointment(Appointment appointment) {
        return appointment.getStartsAt().isBefore(appointment.getFinishesAt());
    }
    private boolean hasDateConflict(Appointment newAppointment) {
        List<Appointment> existingAppointments = appointmentRepository.findAll();
        for (Appointment existingAppointment : existingAppointments) {
            if (existingAppointment.getStartsAt().isBefore(newAppointment.getFinishesAt()) &&
                    existingAppointment.getFinishesAt().isAfter(newAppointment.getStartsAt())) {
                return true; // Hay conflicto de fechas
            }
        }
        return false; // No hay conflicto de fechas
    }




    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<HttpStatus> deleteAppointment(@PathVariable("id") long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);

        if (!appointment.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        appointmentRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/appointments")
    public ResponseEntity<HttpStatus> deleteAllAppointments() {
        appointmentRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
