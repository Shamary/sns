package com.saw.sns.sms.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SMS {
    private String to;
    private String from;
    private String message;
    private String status;
}
