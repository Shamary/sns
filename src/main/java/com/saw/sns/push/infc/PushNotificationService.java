package com.saw.sns.push.infc;

import com.saw.sns.common.SnsResponse;
import com.saw.sns.exception.OperationFailedException;
import com.saw.sns.exception.ValidationErrorException;
import com.saw.sns.push.model.vm.PushNotificationVm;

public interface PushNotificationService {
    public SnsResponse sendNotification(PushNotificationVm pushNotification)
            throws ValidationErrorException, OperationFailedException;
}
