package com.saw.sns.push.model.vm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PushNotificationVm {
    private String message;
    private String topic;
    private String token;
    private String arn;
    private NotificationAttributesVm[] attributes;
}
