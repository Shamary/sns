package com.saw.sns.email.web;

import com.saw.sns.SnsApplicationTests;
import com.saw.sns.common.SnsResponse;
import com.saw.sns.email.model.vm.EmailVm;
import com.saw.sns.email.validation.EmailServiceValidation;
import com.saw.sns.exception.OperationFailedException;
import com.saw.sns.exception.ValidationErrorException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EmailControllerTest extends SnsApplicationTests {
    @Mock
    private EmailServiceValidation emailService;

    @InjectMocks
    private EmailController emailController;

    @Test
    public void testSendEmail_withValidEmail_shouldCallEmailServiceAndReturnSnsResponse() throws Exception {
        EmailVm email = EmailVm.builder()
                .to(new String[]{"test@xyz.com"})
                .from("no-reply@proto.me")
                .message("Test message")
                .build();

        SnsResponse expectedResponse = SnsResponse.builder()
                .message("Message sent")
                .build();
        when(emailService.sendEmail(email)).thenReturn(expectedResponse);

        SnsResponse response = emailController.sendEmail(email);

        assertEquals(expectedResponse, response);
    }

    @Test
    public void testSendEmail_withValidationError_shouldThrowResponseStatusException() {
        EmailVm email = new EmailVm();
        EmailVm.builder()
                .to(new String[]{"test@xyz"})
                .from("no-reply@proto.me")
                .message("Test message")
                .build();

        List<String> errors = new ArrayList<>();
        errors.add("Invalid email address in to field: " + email.getTo());

        when(emailService.sendEmail(email)).thenThrow(new ValidationErrorException(errors));

        assertThrows(ValidationErrorException.class, ()-> emailService.sendEmail(email));
    }

    @Test
    public void testSendEmail_withOperationFailedException_shouldThrowResponseStatusException() {
        EmailVm email = new EmailVm();

        OperationFailedException operationFailedException = new OperationFailedException("Operation failed");
        when(emailService.sendEmail(email)).thenThrow(operationFailedException);

        assertThrows(OperationFailedException.class, () -> emailController.sendEmail(email));
    }
}