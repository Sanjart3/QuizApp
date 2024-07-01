package com.example.quizbackend.admin;

import com.example.quizbackend.general.*;
import com.example.quizbackend.general.Student.UserInfoService;
import com.example.quizbackend.general.repository.RoleRepository;
import com.example.quizbackend.general.Student.UserRepository;
import com.example.quizbackend.general.services.AdminDetailsImpl;
import com.example.quizbackend.usersecurity.jwt.JwtUtils;
import com.example.quizbackend.usersecurity.payload.request.LoginRequest;
import com.example.quizbackend.usersecurity.payload.response.JwtResponse;
import com.example.quizbackend.usersecurity.payload.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    //TODO Need to write controller for admin
    private final AdminService adminService;
    private final UserRepository userRepository;
    private final UserInfoService userInfoService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;

    //TODO need to return JWT token and UserID only
    //@PreAuthorize("hasRole('ROLE_ADMIN')")

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserInfoDTO>> getusers(){
   List<UserInfoDTO> userInfoDTOS =adminService.getAllUsers();
   return  ResponseEntity.ok(userInfoDTOS);

    }
    @GetMapping("/getAdmins")
    public  ResponseEntity<List<UserInfoDTO>> getAdmins(){
        List<UserInfoDTO> userInfoDTOS =adminService.getAllAdmins();
        return  ResponseEntity.ok(userInfoDTOS);
    }
    @PostMapping("/login")
    public ResponseEntity<?> authenticateAdmin(@Validated @RequestBody LoginRequest loginRequest) {
        adminService.defaultAdmin();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtTokenAdmin(authentication);
        AdminDetailsImpl adminDetails = (AdminDetailsImpl) authentication.getPrincipal();
        List<String> roles = adminDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,
                adminDetails.getId(),
                adminDetails.getUsername(),
                adminDetails.getEmail(),
                roles.get(0).toString()));
    }

    //TODO Need to add ADMIN role here
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/adduser")
    public ResponseEntity<?> registerUser(@Validated @RequestBody UserInfo signUpRequest , @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue) throws IOException {
        if (userInfoService.existUserInfoByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userInfoService.existUserInfoByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        if (!roleRepository.findByName(ERole.ROLE_ADMIN).isPresent()) {
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }
        if (!roleRepository.findByName(ERole.ROLE_AF).isPresent()) {
            roleRepository.save(new Role(ERole.ROLE_AF));
        }
        if (!roleRepository.findByName(ERole.ROLE_PROFESSOR).isPresent()) {
            roleRepository.save(new Role(ERole.ROLE_PROFESSOR));
        }
        if (!roleRepository.findByName(ERole.ROLE_STUDENT).isPresent()) {
            roleRepository.save(new Role(ERole.ROLE_STUDENT));
        }
        if (!roleRepository.findByName(ERole.ROLE_TA).isPresent()) {
            roleRepository.save(new Role(ERole.ROLE_TA));
        }
        // Create new user's account
        UserInfo userInfo = new UserInfo(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));
        userInfo.setReg_date(new Date());
        //setting user information coming from front end
        userInfo.setFirst_name(signUpRequest.getFirst_name());
        userInfo.setLast_name(signUpRequest.getLast_name());
        userInfo.setMiddle_name(signUpRequest.getMiddle_name());
        if (signUpRequest.getDepartment() == null) {
            userInfo.setDepartment(Department.SOFTWARE_ENGINEERING);
        } else {
            userInfo.setDepartment(signUpRequest.getDepartment());
        }

        if (signUpRequest.getDob().equals(null)) {
            userInfo.setDob(new Date());
        } else {
            userInfo.setDob(signUpRequest.getDob());
        }
        String strRoles = signUpRequest.getRole();
        List<Role> roles = new ArrayList<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                    .orElseThrow(() -> new RuntimeException("Error1: Role is not found."));
            roles.add(userRole);
        } else {
            switch (strRoles) {
                case "student":
                    Role adminRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                            .orElseThrow(() -> new RuntimeException("Error2: Role is not found."));
                    roles.add(adminRole);
                    break;
                case "professor":
                    Role modRole = roleRepository.findByName(ERole.ROLE_PROFESSOR)
                            .orElseThrow(() -> new RuntimeException("Error3: Role is not found."));
                    roles.add(modRole);
                    break;
                case "ta":
                    Role taRole = roleRepository.findByName(ERole.ROLE_TA)
                            .orElseThrow(() -> new RuntimeException("Error3: Role is not found."));
                    roles.add(taRole);
                    break;
                case "academic":
                    Role academicRole = roleRepository.findByName(ERole.ROLE_AF)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(academicRole);

            }
        }
        userInfo.setRoles(roles);
        userInfo.setRole(roles.get(0).getName().toString());
        userRepository.save(userInfo);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/addUserList")
    public ResponseEntity addUserList(@RequestBody List<UserInfo> userInfos){
        for (UserInfo user:userInfos
             ) {
            userInfoService.save(user);

        }
        return ResponseEntity.ok("All users are added");
    }

    @DeleteMapping("/deleteUserList/{ids}")
    public void deleteUserList(@PathVariable List<Long> ids){
        for (Long id:ids
             ) {
            userInfoService.deleteUserInfoByID(id);
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getuser")
    public ResponseEntity<UserInfoDTO> getUser(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue) {
        UserInfo userInfo = userInfoService.findUserInfoById(id);
        UserInfoDTO userInfoDTO = modelMapper.map(userInfo, UserInfoDTO.class);
        return ResponseEntity.ok(userInfoDTO);
    }

   // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addAdmin")
    public ResponseEntity<?> save(@RequestBody Admin admin) {
        if (adminService.existAdminByUsername(admin.getUsername())) {
            return new ResponseEntity<>("Given username: " + admin.getUsername() + " is already in use!!!", HttpStatus.BAD_REQUEST);
        } else if (adminService.existAdminByEmail(admin.getEmail())) {
            return new ResponseEntity<>("Given email: " + admin.getEmail() + " is already in use!!!", HttpStatus.BAD_REQUEST);
        } else if (adminService.checkLengthPassword(admin.getPassword())) {
            return new ResponseEntity<>("The length of password: " + admin.getPassword() + " is less than 8!!!", HttpStatus.BAD_REQUEST);
        }
        adminService.save(admin);
        return ResponseEntity.ok(admin);
    }

    // get admin data by its id
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/read")
    public ResponseEntity<Admin> readById(@RequestParam Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue) {
        Admin admin = adminService.findAdminById(id);
        return ResponseEntity.ok(admin);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updateById(@RequestParam Long id,
                                        @RequestBody Admin admin, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue) {
        Admin update_admin = adminService.findAdminById(id);
        if (admin.getUsername() != null) {
            if (adminService.existAdminByUsername(admin.getUsername())) {
                return new ResponseEntity<>("Given username: " + admin.getUsername() + " is already in use!!!", HttpStatus.BAD_REQUEST);
            }
            update_admin.setUsername(admin.getUsername());
        } else if (admin.getEmail() != null) {
            if (adminService.existAdminByEmail(admin.getEmail())) {
                return new ResponseEntity<>("Given email: " + admin.getEmail() + " is already in use!!!", HttpStatus.BAD_REQUEST);
            }
            update_admin.setEmail(admin.getEmail());
        } else if (admin.getPassword() != null) {
            if (adminService.checkLengthPassword(admin.getPassword())) {
                return new ResponseEntity<>("The length of password: " + admin.getPassword() + " is less than 8!!!", HttpStatus.BAD_REQUEST);
            }
            update_admin.setPassword(admin.getPassword());
        } else if (admin.getRole() != null) {
            update_admin.setRole(admin.getRole());
        }
        adminService.save(update_admin);
        return ResponseEntity.ok(update_admin);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteadmin")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue) {
        adminService.deleteById(id);
        return ResponseEntity.ok("Admin with ID: " + id + " has been deleted!!!");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/sample")
    public ResponseEntity<?> sample(){
        return ResponseEntity.ok("Hello");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAdmin")

    public ResponseEntity<Admin> findAdminById(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtValue) {
        Admin admin = adminService.findAdminById(id);
        return ResponseEntity.ok(admin);
    }
}
