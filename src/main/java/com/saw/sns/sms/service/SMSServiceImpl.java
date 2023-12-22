package com.saw.sns.sms.service;

import com.saw.sns.sms.infc.SMSService;
import com.saw.sns.sms.model.vm.SMSVm;
import org.springframework.stereotype.Service;
@Service
public class SMSServiceImpl implements SMSService {

    @Override
    public SMSVm SendMessage(String message, String... to) {
        return null;
    }
}
