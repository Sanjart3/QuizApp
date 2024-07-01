package com.example.quizbackend.general.services.images;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface ImagesRepository extends JpaRepository<UserImages, String> {

}