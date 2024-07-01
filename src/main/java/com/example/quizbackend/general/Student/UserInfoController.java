package com.example.quizbackend.general.Student;

import com.example.quizbackend.courses.Courses;
import com.example.quizbackend.general.UserInfo;
import com.example.quizbackend.general.services.UserDetailsImpl;
import com.example.quizbackend.usersecurity.jwt.JwtUtils;
import com.example.quizbackend.usersecurity.payload.request.LoginRequest;
import com.example.quizbackend.usersecurity.payload.response.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@CrossOrigin

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserInfoController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @PutMapping("/updateCourse")
    public ResponseEntity<?> updateCourse(Long id, List<Courses> courses) {
        UserInfo userInfo = userRepository.findUserInfoById(id);
        userInfo.setCoursesList(courses);
        userRepository.save(userInfo);
        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/login")  //changed //TODO need to return JWT token and UserID only
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        String role = roles.toString();
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                role));
    }
}
