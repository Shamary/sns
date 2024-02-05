package com.saw.sns.push.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@ConditionalOnProperty(name = "push.app.s3.config.bucket")
public class FirebaseConfig {

    @Value("${push.app.s3.config.bucket}")
    private String bucket;

    @Value("${push.app.s3.config.file}")
    private String file;

    @Bean
    FirebaseApp firebaseApp() {
        try {

            AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
            S3Object s3Object = s3Client.getObject(bucket, file);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(s3Object.getObjectContent()))
                    .build();

            return FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
