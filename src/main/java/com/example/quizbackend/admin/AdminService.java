package com.example.quizbackend.admin;

import com.example.quizbackend.general.ERole;
import com.example.quizbackend.general.Student.UserRepository;
import com.example.quizbackend.general.UserInfo;
import com.example.quizbackend.general.UserInfoDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private  final UserRepository userRepository;
    private final ModelMapper modelMapper;
    public Admin save(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    public void deleteById(Long id) {
        adminRepository.deleteAdminById(id);
    }

    public Boolean existAdminByEmail(String email) {
        return adminRepository.existsAdminByEmail(email);
    }

    public Boolean existAdminByUsername(String name) {
        return adminRepository.existsAdminByUsername(name);
    }

    public Admin findAdminById(Long id) {
        return adminRepository.findAdminById(id);
    }

    public Boolean checkLengthPassword(String password) {
        return password.length() < 8;
    }



    public Admin defaultAdmin() {
        Admin admin = new Admin();
        if (adminRepository.adminlist().size() == 0) {
            admin.setUsername("admin");
            admin.setEmail("test@gmail.com");
            admin.setPassword(passwordEncoder.encode("1234"));
            admin.setRole(ERole.ROLE_ADMIN);
            adminRepository.save(admin);
        }
        return admin;
    }

    public List<UserInfoDTO> getAllUsers() {
        List<UserInfoDTO> userInfoDTOS =new ArrayList<>();
        List<UserInfo> userInfos =userRepository.findAll();
        for (UserInfo userInfo:userInfos
             ) {
             userInfoDTOS.add(modelMapper.map(userInfo, UserInfoDTO.class));
        }
        return  userInfoDTOS;
    }

    public List<UserInfoDTO> getAllAdmins() {
        List<UserInfoDTO> userInfoDTOS =new ArrayList<>();
        List<Admin> userInfos =adminRepository.findAll();
        for (Admin admin:userInfos
             ) {
            userInfoDTOS.add(modelMapper.map(admin, UserInfoDTO.class));
        }
        return userInfoDTOS;

    }
}
