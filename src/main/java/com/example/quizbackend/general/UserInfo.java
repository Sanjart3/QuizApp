package com.example.quizbackend.general;

import com.example.quizbackend.courses.Courses;
import com.example.quizbackend.general.services.images.UserImages;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.*;

import javax.persistence.*;
@Entity
@Table(name = "users_info")
@Data
@NoArgsConstructor(force = true)
public class UserInfo {
  @Id
  @SequenceGenerator(name = "userGenerator", sequenceName = "USER_SEQUENCE", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "userGenerator")
  @Column(name = "user_id", nullable = false)
  private Long id;
  @Column(name = "username",unique = true,nullable = false)
  private String username;
  @Column(unique = true, length = 250,nullable = false)
  private String email;
  @NonNull
  private String password;
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(  name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
  private List<Role> roles = new ArrayList<>();
  private String first_name;
  private String middle_name;
  private String last_name;
  @Column(unique = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-mm-yyyy")
  private Date dob;
  private Date reg_date;
  @Enumerated(EnumType.STRING)
  private Department department;
  private Boolean active;

  //TODO set from roles to role
  //TODO course
  private String role;

  @Column(name = "reset_password_token", length = 45)
  private String resetPasswordToken;
  @OneToOne(cascade = CascadeType.ALL)
  private UserImages images;
  @ManyToMany(fetch =FetchType.LAZY,mappedBy = "userInfos")
  List<Courses> coursesList;
  public UserInfo(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }
}
