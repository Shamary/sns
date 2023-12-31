package com.saw.sns.common;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SnsResponse {
    String data;
    List<String> errors;
    String message;
}
