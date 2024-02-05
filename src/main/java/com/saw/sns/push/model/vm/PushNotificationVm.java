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
    private String title;
    private String token;
    private String os;
    private String priority;
    private NotificationAttributesVm[] attributes;
}
