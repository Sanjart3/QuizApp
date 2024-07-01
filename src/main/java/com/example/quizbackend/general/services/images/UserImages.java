package com.example.quizbackend.general.services.images;

import javax.persistence.*;

import com.example.quizbackend.general.UserInfo;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@Table(name = "user_images")
public class UserImages {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String name;
    private String type;
    @Lob
    private byte[] data;
    @OneToOne(mappedBy = "images", cascade = CascadeType.REMOVE)
    private UserInfo userInfo;
    public UserImages() {
    }
    public UserImages(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }


}