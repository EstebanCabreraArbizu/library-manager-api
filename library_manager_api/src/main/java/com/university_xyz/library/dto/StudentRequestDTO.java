package com.university_xyz.library.dto;

import lombok.Data;

@Data
/*Podría haber un validador en el nombre del estudiante*/
/*Aunque sería más un extra*/
public class StudentRequestDTO {
    private String name;
}
