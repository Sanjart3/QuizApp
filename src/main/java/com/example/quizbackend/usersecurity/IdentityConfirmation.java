package com.example.quizbackend.usersecurity;

import com.example.quizbackend.usersecurity.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IdentityConfirmation {
    @Autowired
    private JwtUtils jwtUtils;

    public boolean confirmUserIDwithToken(String token, String userID){
        if (jwtUtils.getUserNameFromJwtToken(token.substring(7,token.length())).equals(userID)){
            return true;
        }
        else return false;
    }


}
