package com.example.sahal.Springbootmultithreading2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

import java.util.List;

import static com.example.sahal.Springbootmultithreading2.constant.Constant.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

    @NotNull(message = ID_NOT_NULL_ERROR_MESSAGE)
    private long id;

    @NotBlank(message = FIRSTNAME_MANDATORY_ERROR_MESSAGE)
    @Pattern(regexp = "^[a-zA-Z ]*$", message = FIRSTNAME_PATTERN_ERROR_MESSAGE)
    @Size(min = 3, max = 100, message = FIRSTNAME_SIZE_ERROR_MESSAGE)
    private String firstName;

    @NotBlank(message = LASTNAME_MANDATORY_ERROR_MESSAGE)
    @Pattern(regexp = "^[a-zA-Z ]*$", message = LASTNAME_PATTERN_ERROR_MESSAGE)
    @Size(min = 3, max = 100, message = LASTNAME_SIZE_ERROR_MESSAGE)
    private String lastName;

    @Email(message = EMAIL_PATTERN_ERROR_MESSAGE)
    @NotBlank(message = EMAIL_MANDATORY_ERROR_MESSAGE)
    private String email;

    @NotBlank(message = GENDER_MANDATORY_ERROR_MESSAGE)
    @Pattern(regexp = "^[a-zA-Z ]*$", message = GENDER_PATTERN_ERROR_MESSAGE)
    private String gender;

    @NotNull(message = COMPANY_ID_MANDATORY_ERROR_MESSAGE)
    private long companyId;

    @NotBlank(message = JOB_TITLE_MANDATORY_ERROR_MESSAGE)
    private String jobTitle;

    @NotNull(message = REGISTRATION_ID_MANDATORY_ERROR_MESSAGE)
    private long registrationId;

    @NotNull(message = CITY_ID_MANDATORY_ERROR_MESSAGE)
    private long cityId;
}
