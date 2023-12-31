package com.saw.sns.email.infc;

import com.saw.sns.common.SnsResponse;
import com.saw.sns.email.model.vm.EmailVm;
import com.saw.sns.exception.OperationFailedException;
import com.saw.sns.exception.ValidationErrorException;
import org.springframework.stereotype.Service;

public interface EmailService {
    public SnsResponse sendEmail(EmailVm email) throws ValidationErrorException, OperationFailedException;
}
