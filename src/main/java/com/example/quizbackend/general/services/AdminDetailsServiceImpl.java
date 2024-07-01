package com.example.quizbackend.general.services;

import com.example.quizbackend.admin.Admin;
import com.example.quizbackend.admin.AdminRepository;
import com.example.quizbackend.general.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminDetailsServiceImpl implements UserDetailsService {
    @Autowired
    AdminRepository adminRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin adminInfo = adminRepository.findAdminByUsername(username);
        if (adminInfo == null) {

            return (UserDetails) new UsernameNotFoundException("User Not Found with username: " + username);
        }
        return AdminDetailsImpl.build(adminInfo);
    }
}
