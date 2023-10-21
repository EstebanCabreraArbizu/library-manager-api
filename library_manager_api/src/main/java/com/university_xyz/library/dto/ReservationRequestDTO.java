package com.university_xyz.library.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequestDTO {

    private String bookName;
    /*Validación: Si existe una reserva con el mismo nombre de libro y fecha de reserva
    el sistema debe mostrar un mensaje indicando que la reserva no es posible debido a que ya existe una reserva
    con los datos ingresados. */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservationDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;

    /*Los días de reserva no deben ser cero ni negativos.*/
    @Positive(message = "Los días de la reserva deben ser de mínimo uno")
    @NotNull(message = "Los días de la reserva no deben ser null")
    private Long reservationDays;
    private ReservationStatus status;
    @NotNull(message = "El id del estudiante no debe ser null")
    private Long studentId;
}