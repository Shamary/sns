package com.saw.sns.sms.infc;

import com.saw.sns.sms.model.vm.SMSVm;

public interface SMSService {
    public SMSVm SendMessage(String message, String... to);
}
