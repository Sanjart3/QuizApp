package com.example.quizbackend.general.controllers;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

import com.example.quizbackend.admin.AdminRepository;
import com.example.quizbackend.admin.AdminService;
import com.example.quizbackend.general.UserInfo;
import com.example.quizbackend.general.services.AdminDetailsImpl;
import com.example.quizbackend.professor.ProfessorRepository;
import com.example.quizbackend.professor.ProfessorService;
import com.example.quizbackend.usersecurity.payload.request.LoginRequest;
import com.example.quizbackend.usersecurity.payload.response.JwtResponse;
import com.example.quizbackend.usersecurity.payload.response.MessageResponse;
import com.example.quizbackend.general.repository.RoleRepository;
import com.example.quizbackend.general.Student.UserRepository;
import com.example.quizbackend.usersecurity.jwt.JwtUtils;
import com.example.quizbackend.general.services.UserDetailsImpl;
import javassist.NotFoundException;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;


@CrossOrigin
@RestController

@RequestMapping("/api")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    ProfessorRepository professorRepository;
    @Autowired
    ProfessorService professorService;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    AdminService adminService;
    @Autowired
    AdminRepository adminRepository;

    @PostMapping("/forgot_password")
    public ResponseEntity<?> processForgotPassword(@RequestParam String email, HttpServletRequest request) {
        UserInfo userInfo = professorRepository.findByEmail(email);
        String token = RandomString.make(45);
        try {
            professorService.updateResetPasswordToken(token, email);
            //generate reset password lin
            String resetPasswordLink = Utility.getURL(request) + "/api/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (MessagingException | UnsupportedEncodingException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error while sending email"));
        }

        return ResponseEntity.ok().body(new MessageResponse("Email sent"));
    }

    private void sendEmail(String email, String resetPasswordLink) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("2110031@newuzbekistanuniversity.uz",
                "Students Quiz Support");

        helper.setTo(email);

        String subject = "Here is the link to reset your password";
        String content = "Good part of the day!\n\n" +
                "You have ruqested to reset the password\n" +
                "Clink the link below to change your password\n" +
                "<p><b><a href=\"" + resetPasswordLink + "\">Change My Password</a></b></p>\n" +
                "<p>Ignore this email if you do remember your password, or you have not made the request.</p>";

        helper.setSubject(subject);
        helper.setText(content);

        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public ResponseEntity<?> resetPasswordForm(@Param(value = "token") String token) {
        UserInfo userInfo = professorService.getUserByResetPasswordToken(token);

        if (userInfo == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid token!"));
        }

        return ResponseEntity.ok().body(new MessageResponse("Email sent"));
    }

    @PostMapping("/reset_password")
    public ResponseEntity<?> processResetPassword(
            @RequestParam String password, HttpServletRequest request) {
        String token = request.getParameter("token");


        UserInfo userInfo = professorService.getUserByResetPasswordToken(token);
        if (userInfo == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid token!"));
        } else {
            professorService.updatePassword(userInfo, password);
            return ResponseEntity.ok().body(new MessageResponse("Password changed successfully"));
        }

    }


}
