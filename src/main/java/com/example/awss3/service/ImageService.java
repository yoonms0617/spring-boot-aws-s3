package com.example.awss3.service;

import com.example.awss3.model.Image;
import com.example.awss3.repository.ImageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    private final S3Store s3Store;

    @Transactional
    public void upload(MultipartFile multipartFile) throws IOException {
        String fileName = createFilename(multipartFile.getOriginalFilename());
        String path = s3Store.upload(fileName, multipartFile);
        Image image = Image.builder()
                .name(fileName)
                .path(path)
                .build();
        imageRepository.save(image);
    }

    @Transactional
    public void delete(Long id) {
        Image image = imageRepository.findById(id).orElseThrow();
        s3Store.delete(image.getName());
        imageRepository.delete(image);
    }

    private String createFilename(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + extractExtension(originalFilename);
    }

    private String extractExtension(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
