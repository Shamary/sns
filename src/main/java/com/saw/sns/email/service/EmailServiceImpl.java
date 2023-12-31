package com.saw.sns.email.service;

import com.saw.sns.common.SnsResponse;
import com.saw.sns.email.infc.EmailService;
import com.saw.sns.email.model.vm.EmailVm;
import com.saw.sns.exception.OperationFailedException;
import com.saw.sns.exception.ValidationErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class EmailServiceImpl implements EmailService {
    @Value("${SNS_AWS_REGION}")
    private String awsRegion;
    Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    @Override
    public SnsResponse sendEmail(EmailVm email) throws ValidationErrorException, OperationFailedException {
        SesClient client = SesClient.builder()
                .region(Region.of(awsRegion))
                .build();

        if (email.getCc()== null) {
            email.setCc(new String[]{});
        }

        if (email.getBc() == null) {
            email.setBc(new String[]{});
        }

        Destination destination = Destination.builder()
                .toAddresses(email.getTo())
                .ccAddresses(email.getCc())
                .bccAddresses(email.getBc())
                .build();
        Content message = Content.builder()
                .data(email.getMessage())
                .build();

        Content subject = Content.builder()
                .data(email.getSubject())
                .build();

        Body body = Body.builder()
                .html(message)
                .build();

        Message emailMessage = Message.builder()
                .body(body)
                .subject(subject)
                .build();

        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .source(email.getFrom())
                .destination(destination)
                .message(emailMessage)
                .build();

        try {
            client.sendEmail(sendEmailRequest);
            return SnsResponse.builder()
                    .data("Email sent")
                    .build();
        } catch (SesException e) {
            logger.debug("=========EMAIL " + e.getMessage());
            e.printStackTrace();
            throw new OperationFailedException("Sending email failed");
        }
    }
}
