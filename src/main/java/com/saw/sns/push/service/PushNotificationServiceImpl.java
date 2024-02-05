package com.saw.sns.push.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.saw.sns.common.Constants;
import com.saw.sns.common.SnsResponse;
import com.saw.sns.exception.OperationFailedException;
import com.saw.sns.exception.ValidationErrorException;
import com.saw.sns.push.infc.PushNotificationService;
import com.saw.sns.push.model.vm.NotificationAttributesVm;
import com.saw.sns.push.model.vm.PushNotificationVm;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class PushNotificationServiceImpl implements PushNotificationService {
    @Value("${SNS_AWS_REGION}")
    private String awsRegion;

//    @Value("${push.app.s3.config.bucket}")
//    private String bucket;
//
//    @Value("${push.app.s3.config.file}")
//    private String file;

    Logger logger = LoggerFactory.getLogger(PushNotificationServiceImpl.class);

    // @PostConstruct
//    private void initFirebase() {
//        try {
//
//            AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
//            S3Object s3Object = s3Client.getObject(bucket, file);
//
//            FirebaseOptions options = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.fromStream(s3Object.getObjectContent()))
//                    .build();
//
//            FirebaseApp.initializeApp(options);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public SnsResponse sendNotification(PushNotificationVm pushNotification)
            throws ValidationErrorException, OperationFailedException {

        String priority = !StringUtils.hasText(pushNotification.getPriority()) ? Constants.PRIORITY_NORMAL
                : pushNotification.getPriority();

        // priority for ios
        if (StringUtils.hasText(pushNotification.getOs())) {
            if (pushNotification.getOs().equals(Constants.OS_IOS)) {
                if (priority.equals(Constants.PRIORITY_HIGH)) {
                    priority = "10";
                } else {
                    priority = "5";
                }
            }
        }

        Map attributes = new HashMap<String, String>();

        // create message attributes from request attributes field
        if (pushNotification.getAttributes() != null) {
            for (NotificationAttributesVm attributesVm : pushNotification.getAttributes()) {
                attributes.put(attributesVm.getKey(),
                        MessageAttributeValue.builder().stringValue(attributesVm.getValue()).build());
            }
        }

        Message fcmMessage = buildFcmMessage(pushNotification, attributes, priority);

        try {
            String response = FirebaseMessaging.getInstance().send(fcmMessage);
        } catch (FirebaseMessagingException e) {
            logger.error("=======PUSH ERROR " + e.getMessage());
            throw new OperationFailedException("error sending push notification");
        }

        return SnsResponse.builder().message("Message sent").build();
    }

    public static Message buildFcmMessage(PushNotificationVm pushNotification, Map attributes, String priority) {
        Message.Builder messageBuilder = Message.builder()
                .putData("title", pushNotification.getTitle())
                .putData("message", pushNotification.getMessage());

        if (StringUtils.hasText(pushNotification.getTopic())) {
            messageBuilder.setTopic(pushNotification.getTopic());
        } else if (StringUtils.hasText(pushNotification.getToken())) {
            messageBuilder.setToken(pushNotification.getToken());
        }

        if (StringUtils.hasText(pushNotification.getOs())) {
            String os = pushNotification.getOs().toLowerCase();
            if (os.equals(Constants.OS_ANDROID)) {
                messageBuilder.setAndroidConfig(AndroidConfig.builder()
                        .putAllData(attributes)
                        .setPriority(AndroidConfig.Priority.valueOf(priority))
                        .build());
            } else if (os.equals(Constants.OS_IOS)) {
                messageBuilder.setApnsConfig(ApnsConfig.builder()
                        .putAllCustomData(attributes)
                        .putHeader("apn-priority", priority)
                        .build());
            }
        }

        return messageBuilder.build();
    }
}
