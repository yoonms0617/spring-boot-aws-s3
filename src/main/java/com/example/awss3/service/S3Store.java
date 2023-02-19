package com.example.awss3.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class S3Store {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final S3Client s3Client;

    public String upload(String filename, MultipartFile multipartFile) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .contentType(multipartFile.getContentType())
                .key(filename) // S3 bucket에 폴더가 존재할 시 앞에 "폴더명 + /"를 붙여서 key 값으로 줘야함
                .bucket(bucket)
                .build();
        RequestBody requestBody = RequestBody.fromInputStream(
                multipartFile.getInputStream(), multipartFile.getInputStream().available()
        );
        s3Client.putObject(putObjectRequest, requestBody);
        URL url = s3Client.utilities().getUrl(GetUrlRequest.builder().key(filename).bucket(bucket).build());
        return url.toString();
    }

    public void delete(String name) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .key(name)
                .bucket(bucket)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }


}
