package com.example.quizbackend.professor;

import com.example.quizbackend.general.GeneralInfoUser;
import com.example.quizbackend.general.UserInfo;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfessorService {
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private final ProfessorRepository professorRepository;
    @Autowired
    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }
 GeneralInfoUser getGeneralInfo(String username){
        return professorRepository.getByUsernameGeneral(username);
}
//Professor can update its own few information
    public Boolean updateProfessor(String username, UserInfo professor) {
        if (professorRepository.existsByUsername(username)) {
            UserInfo existingProfessor = professorRepository.getByUsernameAllInfo(username);
            existingProfessor.setPassword(encoder.encode(professor.getPassword()));
            existingProfessor.setEmail(professor.getEmail());
            existingProfessor.setDob(professor.getDob());
            existingProfessor.setFirst_name(professor.getFirst_name());
            existingProfessor.setMiddle_name(professor.getMiddle_name());
            existingProfessor.setLast_name(professor.getLast_name());
            professorRepository.save(existingProfessor);
            return true;
        }
        else    throw new IllegalStateException("Professor couldn't find from database");
    }

    public void updateResetPasswordToken(String token, String email) throws NotFoundException {
        UserInfo userInfo = professorRepository.findByEmail(email);

        if(userInfo != null){
            userInfo.setResetPasswordToken(token);
            professorRepository.save(userInfo);
        } else {
            throw new NotFoundException("Could not found any user with this email: "+email);
        }
    }

    public UserInfo getUserByResetPasswordToken(String resetPasswordToken){
        return professorRepository.findByResetPasswordToken(resetPasswordToken);
    }

    public void updatePassword(UserInfo userInfo, String newPassword ){
        String encodePassword = encoder.encode(newPassword);

        userInfo.setPassword(encodePassword);
        userInfo.setResetPasswordToken(null);

        professorRepository.save(userInfo);
    }

    public String getProfessorUserNameById(Long professorId){
        return professorRepository.getUsernameById(professorId);
    }

    public String getUsernameById(Long userId){
        return professorRepository.getUsernameById(userId);
    }

    public Boolean existByUsername(String professor) {
        return professorRepository.existsByUsername(professor);
    }

    public Boolean existByEmail(String email) {
        return professorRepository.existsByEmail(email);
    }

    public Boolean checkPasswordLength(String password) {
        return password.length() < 8;
    }
//There is no delete option for professor Because it is critical information
    //We can simply make status inactive but we should not delete professor from database once it is created
    //We should apply same rule for students also, We should not delete any student data
    //We can only update some information about student, and students also not allowed to update their information
    //They can only change phone number


}
