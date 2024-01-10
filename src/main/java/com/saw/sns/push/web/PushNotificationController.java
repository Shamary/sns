package com.saw.sns.push.web;

import com.saw.sns.common.SnsResponse;
import com.saw.sns.exception.OperationFailedException;
import com.saw.sns.exception.ValidationErrorException;
import com.saw.sns.push.infc.PushNotificationService;
import com.saw.sns.push.model.vm.PushNotificationVm;
import com.saw.sns.push.validation.PushNotificationServiceValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sns")
public class PushNotificationController implements PushNotificationService {
    @Autowired
    private PushNotificationServiceValidation pushNotificationService;
    @Override
    @PostMapping("/push")
    public SnsResponse sendNotification(@RequestBody PushNotificationVm pushNotification) throws ValidationErrorException, OperationFailedException {
        return pushNotificationService.sendNotification(pushNotification);
    }
}
