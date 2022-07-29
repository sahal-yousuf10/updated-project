package com.example.sahal.Springbootmultithreading2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDto {

    private long id;
    private String name;
    private ErrorDto errors;
}
