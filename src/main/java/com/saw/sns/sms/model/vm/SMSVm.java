package com.saw.sns.sms.model.vm;

import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SMSVm {
    private String to;
    private String from;
    private String message;
    private String status;
}
