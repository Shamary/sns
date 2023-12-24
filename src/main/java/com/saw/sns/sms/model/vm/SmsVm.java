package com.saw.sns.sms.model.vm;

import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SmsVm {
    private String id;
    private String[] to;
    private String message;
    private String status;
}
