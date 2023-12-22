package com.saw.sns.sms.validation;

import com.saw.sns.exception.OperationFailedException;
import com.saw.sns.exception.ValidationErrorException;
import com.saw.sns.sms.infc.SmsService;
import com.saw.sns.sms.model.vm.SmsVm;
import com.saw.sns.sms.service.SmsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SmsServiceValidation implements SmsService {

    @Autowired
    SmsServiceImpl smsService;

    @Override
    public SmsVm SendMessage(String message, String... to) throws ValidationErrorException, OperationFailedException {
        List<String> errors = validate(message, to);
        if (errors.size() > 0)
        {
            throw new ValidationErrorException(errors);
        }
        return smsService.SendMessage(message, to);
    }
    private List<String> validate(String message, String... to) {
        List<String> errors = new ArrayList<>();

        // Validate message
        if (message == null || message.isEmpty()) {
            errors.add("Message must be provided");
        }

        // Validate to field
        if (to == null || to.length == 0) {
            errors.add("To field must be provided");
        } else {
            for (String phoneNumber : to) {
                if (phoneNumber == null || phoneNumber.isEmpty()) {
                    errors.add("Cannot contain empty phone numbers.");
                } else if (!phoneNumber.matches("\\d+")) {
                    errors.add("Phone number '" + phoneNumber + "' must contain only digits.");
                }
            }
        }
        return errors;
    }

}
