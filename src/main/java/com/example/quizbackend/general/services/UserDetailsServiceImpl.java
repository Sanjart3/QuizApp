package com.example.quizbackend.general.services;

import com.example.quizbackend.admin.Admin;
import com.example.quizbackend.admin.AdminRepository;
import com.example.quizbackend.general.UserInfo;
import com.example.quizbackend.general.Student.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AdminRepository adminRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (adminRepository.existsAdminByUsername(username)) {
            Admin admin = adminRepository.findAdminByUsername(username);
            return AdminDetailsImpl.build(admin);
        }
        UserInfo userInfo = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User Not Found with username: " + username));
        return UserDetailsImpl.build(userInfo);
    }

}
