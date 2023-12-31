package com.saw.sns.email.model.vm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class EmailVm {
    private String from;
    private String[] to;
    private String[] cc;
    private String[] bc;
    private String subject;
    private String message;
    private String attachment;
}
