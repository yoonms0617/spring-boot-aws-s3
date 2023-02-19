## Spring Boot + AWS S3

### # 개발 환경

- Java 17.0.6
- Spring Boot v3.0.2

### # application-aws.yml

```yaml
cloud:
  aws:
    region: { REGION }
    s3:
      credentials:
        access-key: { ACCESS_KEY }
        secret-key: { SECRET_KEY }
      bucket: { BUCKET }
```

#### 참고

- [S3 File upload & download with AWS Java SDK v2](https://www.javacodemonk.com/aws-java-sdk-2-s3-file-upload-download-spring-boot-c1a3e072)