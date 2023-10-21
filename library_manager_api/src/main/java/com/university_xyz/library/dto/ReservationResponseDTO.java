package com.university_xyz.library.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationResponseDTO {
    private Long id;
    private String bookName;
    private LocalDate reservationDate;
    private LocalDate returnDate;
    private Long reservationDays;
    private ReservationStatus status;
    private StudentResponseDTO student;
}
