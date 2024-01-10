package com.saw.sns.push.service;

import com.saw.sns.common.SnsResponse;
import com.saw.sns.exception.OperationFailedException;
import com.saw.sns.exception.ValidationErrorException;
import com.saw.sns.push.infc.PushNotificationService;
import com.saw.sns.push.model.vm.NotificationAttributesVm;
import com.saw.sns.push.model.vm.PushNotificationVm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.HashMap;
import java.util.Map;

@Service
public class PushNotificationServiceImpl implements PushNotificationService {
    @Value("${SNS_AWS_REGION}")
    private String awsRegion;
    @Override
    public SnsResponse sendNotification(PushNotificationVm pushNotification) throws ValidationErrorException, OperationFailedException {
        SnsClient snsClient = SnsClient.builder()
                .region(Region.of(awsRegion))
                .build();

        PublishRequest request = PublishRequest.builder().build();
        Map<String, MessageAttributeValue> attributes = new HashMap<>();

        // create message attributes from request attributes field
        if (pushNotification.getAttributes() != null) {
            for (NotificationAttributesVm attributesVm : pushNotification.getAttributes()) {
                attributes.put(attributesVm.getKey(),
                        MessageAttributeValue.builder().stringValue(attributesVm.getValue()).build());
            }
        }

        if (!StringUtils.isEmpty(pushNotification.getTopic())) {
            request = PublishRequest.builder()
                    .message(pushNotification.getMessage())
                    .topicArn(pushNotification.getTopic())
                    .messageAttributes(attributes)
                    .build();
        }
        else if(StringUtils.isEmpty(pushNotification.getTopic())) {
            request = PublishRequest.builder()
                    .message(pushNotification.getMessage())
                    .targetArn(pushNotification.getToken())
                    .messageAttributes(attributes)
                    .build();
        }

        try {
            PublishResponse publishResponse = snsClient.publish(request);
        } catch (Exception e) {
            throw new OperationFailedException("Failed to send push notification");
        } finally {
            snsClient.close();
        }

        return SnsResponse.builder().message("Message sent").build();
    }
}
