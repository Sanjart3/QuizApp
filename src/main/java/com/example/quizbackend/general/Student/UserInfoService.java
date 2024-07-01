package com.example.quizbackend.general.Student;

import com.example.quizbackend.courses.Courses;
import com.example.quizbackend.general.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public UserInfo save(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        return userRepository.save(userInfo);
    }

    public UserInfo updateCourse(Long id, List<Courses> courses) {
        UserInfo userInfo = userRepository.findUserInfoById(id);
        userInfo.setCoursesList(courses);
        userRepository.save(userInfo);
        return userInfo;
    }

    public UserInfo findUserInfoByUsername(String username) {
        return userRepository.findUserInfoByUsername(username);
    }

    public UserInfo findUserInfoById(Long id) {
        return userRepository.findUserInfoById(id);
    }

    public Boolean existUserInfoByUsername(String username) {
        return userRepository.existsUserInfoByUsername(username);
    }

    public Boolean existUserInfoByEmail(String email) {
        return userRepository.existsUserInfoByEmail(email);
    }

    public void deleteUserInfoByID(Long id) {
        UserInfo userInfo =userRepository.findUserInfoById(id);
        userInfo.setRoles(null);


        userRepository.deleteById(id);
    }


}
