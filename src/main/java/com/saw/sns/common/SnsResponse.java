package com.saw.sns.common;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SnsResponse {
    String data;
    String error;
    String message;
}
