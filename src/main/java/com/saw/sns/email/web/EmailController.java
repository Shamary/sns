package com.saw.sns.email.web;

import com.saw.sns.common.SnsResponse;
import com.saw.sns.email.infc.EmailService;
import com.saw.sns.email.model.vm.EmailVm;
import com.saw.sns.email.validation.EmailServiceValidation;
import com.saw.sns.exception.OperationFailedException;
import com.saw.sns.exception.ValidationErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/sns")
public class EmailController implements EmailService {
    @Autowired
    private EmailServiceValidation emailService;

    @Override
    @PostMapping("/email")
    public SnsResponse sendEmail(@RequestBody EmailVm email) throws ValidationErrorException, OperationFailedException {
        return emailService.sendEmail(email);
    }
}
