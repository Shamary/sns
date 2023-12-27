package com.saw.sns.sms.web;

import com.saw.sns.SnsApplicationTests;
import com.saw.sns.common.SnsResponse;
import com.saw.sns.exception.OperationFailedException;
import com.saw.sns.exception.ValidationErrorException;
import com.saw.sns.sms.model.vm.SmsVm;
import com.saw.sns.sms.service.SmsServiceImpl;
import com.saw.sns.sms.validation.SmsServiceValidation;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@SpringBootTest
class SmsControllerTest extends SnsApplicationTests {
    @Mock
    SmsServiceValidation smsService;

    @InjectMocks
    SmsController smsController;

    @Test
    public void testSendMessageWithValidInput() throws ValidationErrorException, OperationFailedException {
        SmsVm smsVm = new SmsVm();
        smsVm.setMessage("Hello");
        smsVm.setTo(new String[]{"+1234567890"});
        smsVm.setId("76e51137-bd16-5c39-aef7-5f36dc074323");

        SnsResponse expectedResult = SnsResponse.builder()
                .message("Message sent")
                .build();;

        when(smsService.SendMessage(smsVm)).thenReturn(expectedResult);

        SnsResponse result = smsController.SendMessage(smsVm);

        assertEquals(expectedResult, result);
        verify(smsService, times(1)).SendMessage(smsVm);
    }

    @Test
    public void testSendMessageWithValidationError() throws ValidationErrorException, OperationFailedException {
        SmsVm smsVm = new SmsVm();
        smsVm.setTo(new String[]{"+18761234567"});

        smsVm.setMessage(""); // empty message

        List<String> errors = new ArrayList<>();
        errors.add("Message must be provided");

        when(smsService.SendMessage(smsVm)).thenThrow(new ValidationErrorException(errors));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            smsController.SendMessage(smsVm);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(smsService, times(1)).SendMessage(smsVm);
    }

    @Test
    public void testSendMessageWithOperationFailedException() throws ValidationErrorException, OperationFailedException {
        SmsVm smsVm = new SmsVm();
        smsVm.setMessage("Hello");
        smsVm.setTo(new String[]{"1234567890"});

        when(smsService.SendMessage(smsVm)).thenThrow(new OperationFailedException("Operation failed"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            smsController.SendMessage(smsVm);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        verify(smsService, times(1)).SendMessage(smsVm);
    }
}