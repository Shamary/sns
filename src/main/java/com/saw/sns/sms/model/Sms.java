package com.saw.sns.sms.model;

//import jakarta.persistence.*;
import lombok.*;

//@Entity
//@Table(name = "Sms")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Sms {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String to;
    private String from;
    private String message;
    private String status;
}
