package com.university_xyz.library.controller;

import com.university_xyz.library.dto.ReservationRequestDTO;
import com.university_xyz.library.dto.ReservationResponseDTO;
import com.university_xyz.library.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/*Controlador de las reservas de libros*/

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService taskService;

    @PostMapping
    public ReservationResponseDTO createReservation(@Valid @RequestBody ReservationRequestDTO reservationRequest){
        return taskService.createReservation(reservationRequest);
    }

    /*Puede que este mal implementado*/
    @GetMapping("/range")
    public List<ReservationResponseDTO> getAllTaskByDateRange(
            @RequestParam("start_date") LocalDate startDate,
            @RequestParam("end_date")LocalDate endDate
    ){
        return taskService.getReservationsByDateRange(startDate,endDate);
    }

    @GetMapping("{student_name}")
    public List<ReservationResponseDTO> getAllTaskByStudent(@PathVariable String student_name)
    {
        return taskService.getReservationsByStudentName(student_name);
    }
}

