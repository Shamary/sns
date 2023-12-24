package com.saw.sns.sms.validation;

import com.saw.sns.SnsApplicationTests;
import com.saw.sns.exception.OperationFailedException;
import com.saw.sns.exception.ValidationErrorException;
import com.saw.sns.sms.model.vm.SmsVm;
import com.saw.sns.sms.service.SmsServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

//@SpringBootTest
class SmsServiceValidationTest extends SnsApplicationTests {
    @InjectMocks
    SmsServiceValidation smsService;

    @Mock
    SmsServiceImpl smsServiceMock;

    @Test
    public void testSendMessageWithValidInput() throws ValidationErrorException, OperationFailedException {
        SmsVm smsVm = new SmsVm();
        smsVm.setMessage("Hello");
        smsVm.setTo(new String[]{"+18761234567"});

        // Specify the behavior of SendMessage in the mock
//        SmsVm smsVmResponse = SmsVm.builder()
//                .message("Hello")
//                .to(new String[]{"+18761234567"})
//                .id("76e51137-bd16-5c39-aef7-5f36dc074323")
//                .build();

        String expectedResult = "Message sent";

        when(smsServiceMock.SendMessage(any(SmsVm.class))).thenReturn(expectedResult);

        String result = smsService.SendMessage(smsVm);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testSendMessageWithInvalidMessage() {
        SmsVm smsVm = new SmsVm();
        smsVm.setMessage(""); // empty message
        smsVm.setTo(new String[]{"+18761234567"});

        ValidationErrorException exception = assertThrows(ValidationErrorException.class, () -> {
            smsService.SendMessage(smsVm);
        });

        List<String> expectedErrors = Collections.singletonList("Message must be provided");
        assertEquals(expectedErrors, exception.getErrors());
    }

    @Test
    public void testSendMessageWithInvalidToField() {
        SmsVm smsVm = new SmsVm();
        smsVm.setMessage("Hello");

        ValidationErrorException exception = assertThrows(ValidationErrorException.class, () -> {
            smsService.SendMessage(smsVm);
        });

        List<String> expectedErrors = Collections.singletonList("To field must be provided");
        assertEquals(expectedErrors, exception.getErrors());
    }

    @Test
    public void testSendMessageWithInvalidPhoneNumber() {
        SmsVm smsVm = new SmsVm();
        smsVm.setMessage("Hello");
        smsVm.setTo(new String[]{"+187633S900B"});

        ValidationErrorException exception = assertThrows(ValidationErrorException.class, () -> {
            smsService.SendMessage(smsVm);
        });

        List<String> expectedErrors = Collections.singletonList("Phone number '+187633S900B' must contain only digits.");
        assertEquals(expectedErrors, exception.getErrors());
    }
}