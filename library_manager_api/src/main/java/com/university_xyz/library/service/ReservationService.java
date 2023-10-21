package com.university_xyz.library.service;

import com.university_xyz.library.dto.StudentResponseDTO;
import com.university_xyz.library.dto.ReservationRequestDTO;
import com.university_xyz.library.dto.ReservationResponseDTO;
import com.university_xyz.library.dto.ReservationStatus;
import com.university_xyz.library.exception.ResourceNotFoundException;
import com.university_xyz.library.exception.ResourceValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/*Funciones a implementar:
* Reserva de Libros
    * La fecha de devolución no se ingresa en una reserva; se debe generar automáticamente sumando los días de reserva a la fecha de reserva.
    * Si existe una reserva con el mismo nombre de libro y fecha de reserva, no se podrá hacer la reserva
    * Un estudiante solo puede realizar un máximo de 5 reservas
* Listado de Reservas por rango de fecha
    * El sistema debe validar que la fecha de finalización de la búsqueda no sea anterior a la fecha de inicio, caso contrario mostrar un mensaje de error
* Listado por reservas por estudiante */
@Service
public class ReservationService {
    private final List<ReservationResponseDTO> reservations = new ArrayList<>();
    private final List<StudentResponseDTO> students = new ArrayList<>();

    private final Validator validator;

    private Long reservationIdCounter = 1L;

    public ReservationService(Validator validator){
        this.validator = validator;
        this.loadStudents();
    }


    public ReservationResponseDTO createReservation(ReservationRequestDTO reservationRequest){
        Set<ConstraintViolation<ReservationRequestDTO>> violations = validator.validate(reservationRequest);

        if(!violations.isEmpty()){
            throw new ResourceValidationException("Reservation", violations);
        }

        // Si existe una reserva con el mismo nombre de libro y fecha de reserva, no se podrá hacer la reserva
        validateDuplicateReservations(reservationRequest);
        validateReservationsByDateRange(reservationRequest);

        StudentResponseDTO student= students
                .stream()
                .filter(p-> p.getId().equals(reservationRequest.getStudentId()))
                .findFirst()
                .orElseThrow(()-> new ResourceValidationException("El estudiante con el ID " + reservationRequest.getStudentId() + " no existe"));
        //* La fecha de devolución no se ingresa en una reserva; se debe generar automáticamente sumando los días de reserva a la fecha de reserva.
        Long days = ChronoUnit.DAYS.between(reservationRequest.getReservationDate(), reservationRequest.getReturnDate());
        ReservationResponseDTO newreservation = new ReservationResponseDTO();
        newreservation.setId(reservationIdCounter++);
        newreservation.setBookName(reservationRequest.getBookName());
        newreservation.setReservationDate(reservationRequest.getReservationDate());
        newreservation.setReturnDate(reservationRequest.getReturnDate());
        newreservation.setReservationDays(days);
        newreservation.setStatus(ReservationStatus.Reserved);
        newreservation.setStudent(student);

        reservations.add(newreservation);

        return newreservation;

    }


    public List<ReservationResponseDTO> getReservationsByDateRange(LocalDate startDate, LocalDate endDate){
        if(endDate.isBefore(startDate))
            throw  new ResourceValidationException("La fecha de fin no debe ser menor a la fecha de inicio");

        return reservations.stream()
                .filter(Reservation->!Reservation.getReservationDate().isBefore(startDate) && !Reservation.getReservationDate().isAfter(endDate))
                .toList();
    }

    public List<ReservationResponseDTO> getReservationsByStudentName(String studentName){
        return reservations.stream()
                .filter(reservation-> reservation.getStudent().getName().equalsIgnoreCase(studentName))
                .toList();
    }

    private void loadStudents(){
        StudentResponseDTO student1 = new StudentResponseDTO();
        student1.setId(1L);
        student1.setName("John Doe");

        StudentResponseDTO student2 = new StudentResponseDTO();
        student2.setId(2L);
        student2.setName("Esteban Arbizu");

        students.add(student1);
        students.add(student2);
    }


    private void validateDuplicateReservations(ReservationRequestDTO reservationRequest){
        boolean taskAlreadyExists = reservations
                .stream()
                .anyMatch(existingReservation -> existingReservation.getBookName().equals(reservationRequest.getBookName()) &&
                        existingReservation.getReservationDate().equals(reservationRequest.getReservationDate()));

        if(taskAlreadyExists){
            throw  new ResourceValidationException("Ya existe una reserva con el mismo nombre y fecha de asignacion");
        }
    }
    private void validateReservationsByDateRange(ReservationRequestDTO reservationRequest) {
        if (reservationRequest.getReturnDate().isBefore(reservationRequest.getReservationDate()))
            throw new ResourceValidationException("La fecha de fin no debe ser menor a la fecha de inicio");
    }
}
