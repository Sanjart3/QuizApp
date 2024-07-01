package com.example.quizbackend.general;

import com.example.quizbackend.general.services.images.UserImages;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoDTO {
    private Long id;
    private String username;
    private String first_name;
    private String middle_name;
    private String last_name;
    private String email;
    private String password;
    private String role;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-mm-yyyy")
    private Date dob;
    private Department department;
    private UserImages images;
}
