package com.example.quizbackend.general.services.images;

import com.example.quizbackend.general.Student.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public class UserImagesService {
    @Autowired
    private ImagesRepository fileDBRepository;
    @Autowired
    private UserRepository userRepository;

    public void store(MultipartFile file, String username) throws IOException {
        if( userRepository.findByUsername(username).get().getImages()!=null)
        {
            System.out.println("NOT NULL!");
            UserImages existingImage=userRepository.findByUsername(username).get().getImages();
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            existingImage.setData(file.getBytes());
            existingImage.setName(fileName);
            existingImage.setType(file.getContentType());
            fileDBRepository.save(existingImage);
        }else {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            UserImages UserImages = new UserImages(fileName, file.getContentType(), file.getBytes());
            userRepository.findByUsername(username).get().setImages(UserImages);
            fileDBRepository.save(UserImages);
        }
    }
    public UserImages getFile(String id) {
        return fileDBRepository.findById(id).get();
    }
}
