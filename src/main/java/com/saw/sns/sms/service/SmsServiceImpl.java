package com.saw.sns.sms.service;

import com.saw.sns.exception.OperationFailedException;
import com.saw.sns.exception.ValidationErrorException;
import com.saw.sns.sms.infc.SmsService;
import com.saw.sns.sms.model.vm.SmsVm;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class SmsServiceImpl implements SmsService {
    @Override
    public SmsVm SendMessage(String message, String... to) throws ValidationErrorException, OperationFailedException {
        return null;
    }
}
