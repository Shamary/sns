package com.saw.sns.push.model.vm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class NotificationAttributesVm {
    private String key;
    private String value;
}
