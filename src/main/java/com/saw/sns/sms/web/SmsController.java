package com.saw.sns.sms.web;

import com.saw.sns.exception.OperationFailedException;
import com.saw.sns.exception.ValidationErrorException;
import com.saw.sns.sms.infc.SmsService;
import com.saw.sns.sms.model.vm.SmsVm;
import com.saw.sns.sms.validation.SmsServiceValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sns")
public class SmsController implements SmsService {
    @Autowired
    private SmsServiceValidation smsService;
    @Override
    @PostMapping("/sms")
    public SmsVm SendMessage(String message, String... to) throws ValidationErrorException, OperationFailedException {
        try {
            return smsService.SendMessage(message, to);
        } catch(ValidationErrorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
