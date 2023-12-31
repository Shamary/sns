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
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.PreencodedMimeBodyPart;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class EmailServiceImpl implements EmailService {
    @Value("${SNS_AWS_REGION}")
    private String awsRegion;
    Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    @Override
    public SnsResponse sendEmail(EmailVm email) throws ValidationErrorException, OperationFailedException {
        return sendEmailWithAttachment(email);
//        SesClient client = SesClient.builder()
//                .region(Region.of(awsRegion))
//                .build();
//
//        if (email.getCc()== null) {
//            email.setCc(new String[]{});
//        }
//
//        if (email.getBc() == null) {
//            email.setBc(new String[]{});
//        }
//
//        Destination destination = Destination.builder()
//                .toAddresses(email.getTo())
//                .ccAddresses(email.getCc())
//                .bccAddresses(email.getBc())
//                .build();
//        Content message = Content.builder()
//                .data(email.getMessage())
//                .build();
//
//        Content subject = Content.builder()
//                .data(email.getSubject())
//                .build();
//
//        Body body = Body.builder()
//                .html(message)
//                .build();
//
//        Message emailMessage = Message.builder()
//                .body(body)
//                .subject(subject)
//                .build();
//
//        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
//                .source(email.getFrom())
//                .destination(destination)
//                .message(emailMessage)
//                .build();
//
//        try {
//            client.sendEmail(sendEmailRequest);
//            return SnsResponse.builder()
//                    .data("Email sent")
//                    .build();
//        } catch (SesException e) {
//            logger.debug("=========EMAIL " + e.getMessage());
//            e.printStackTrace();
//            throw new OperationFailedException("Sending email failed");
//        }
    }

    private SnsResponse sendEmailWithAttachment(EmailVm emailVm) {
        SesClient client = SesClient.builder()
                .region(Region.of(awsRegion))
                .build();
        Session session = Session.getDefaultInstance(new Properties());

        try {
            MimeMessage message = new MimeMessage(session);
            message.setSubject(emailVm.getSubject());
            message.setFrom(emailVm.getFrom());

            String to = Arrays.toString(emailVm.getTo());

            message.setRecipients(javax.mail.Message.RecipientType.TO, to.substring(1, to.length() -1));

            if (!StringUtils.isEmpty(emailVm.getCc())) {
                String cc = Arrays.toString(emailVm.getCc());
                message.setRecipients(javax.mail.Message.RecipientType.CC, cc.substring(1, cc.length() -1));
            }

            if (!StringUtils.isEmpty(emailVm.getBc())) {
                String bc = Arrays.toString(emailVm.getBc());
                message.setRecipients(javax.mail.Message.RecipientType.BCC, bc.substring(1, bc.length() -1));
            }
            
            MimeMultipart msg = new MimeMultipart("mixed");

            // Create the HTML part.
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(emailVm.getMessage(), "text/html; charset=UTF-8");

            // Add the HTML part to the message.
            msg.addBodyPart(htmlPart);

            if (!StringUtils.isEmpty(emailVm.getAttachment())) {
                // Define the attachment.
                MimeBodyPart att = new PreencodedMimeBodyPart("base64");
                att.setContent(emailVm.getAttachment(), "application/octet-stream");
                att.setFileName(emailVm.getFileName());

                // Add the attachment to the message.
                msg.addBodyPart(att);
            }

            // Set the content of the message.
            message.setContent(msg);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            message.writeTo(outputStream);

            ByteBuffer buf = ByteBuffer.wrap(outputStream.toByteArray());

            byte[] arr = new byte[buf.remaining()];
            buf.get(arr);

            SdkBytes data = SdkBytes.fromByteArray(arr);
            RawMessage rawMessage = RawMessage.builder()
                    .data(data)
                    .build();

            SendRawEmailRequest rawEmailRequest = SendRawEmailRequest.builder()
                    .rawMessage(rawMessage)
                    .build();

            client.sendRawEmail(rawEmailRequest);

            return SnsResponse.builder()
                    .message("Message sent")
                    .build();
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new OperationFailedException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new OperationFailedException(e.getMessage());
        }
    }
}
