package com.saw.sns.push.validation;

import com.saw.sns.common.Constants;
import com.saw.sns.common.SnsResponse;
import com.saw.sns.exception.OperationFailedException;
import com.saw.sns.exception.ValidationErrorException;
import com.saw.sns.push.infc.PushNotificationService;
import com.saw.sns.push.model.vm.NotificationAttributesVm;
import com.saw.sns.push.model.vm.PushNotificationVm;
import com.saw.sns.push.service.PushNotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
@Service
public class PushNotificationServiceValidation implements PushNotificationService {
    @Autowired
    private PushNotificationServiceImpl pushNotificationService;

    @Override
    public SnsResponse sendNotification(PushNotificationVm pushNotification)
            throws ValidationErrorException, OperationFailedException {
        List<String> errors = validate(pushNotification);
        if (errors.size() > 0) {
            throw new ValidationErrorException(errors);
        }
        return pushNotificationService.sendNotification(pushNotification);
    }

    public List<String> validate(PushNotificationVm vm) {
        List<String> errors = new ArrayList<>();

        if (isBlank(vm.getMessage())) {
            errors.add("Message must be provided.");
        }

        // Check topic/token relationship
        if (isBlank(vm.getTopic()) && isBlank(vm.getToken())) {
            errors.add("Either topic or token must be provided.");
        } else if (isNotBlank(vm.getTopic()) && isNotBlank(vm.getToken())) {
            errors.add("Only one of topic or token can be provided.");
        }

        if (StringUtils.hasText(vm.getPriority())) {
            if (!vm.getPriority().equals(Constants.PRIORITY_HIGH) && !vm.getPriority().equals(Constants.PRIORITY_NORMAL)) {
                errors.add("priority must be either HIGH or NORMAL");
            }
        }

        // Check attributes
        if (vm.getAttributes() != null) {
            if (isBlank(vm.getOs())) {
                errors.add("os must be provided to send data messages");
            }
            else
            {
                if (!vm.getOs().equals(Constants.OS_ANDROID) && !vm.getOs().equals(Constants.OS_IOS)) {
                    errors.add("os must be either android or ios");
                }
            }

            for (NotificationAttributesVm attribute : vm.getAttributes()) {
                if (isBlank(attribute.getKey()) || isBlank(attribute.getValue())) {
                    errors.add("Both key and value must be provided for each attribute.");
                }
            }
        }

        return errors;
    }
    private boolean isBlank(String string)
    {
        return !StringUtils.hasText(string);
    }

    private boolean isNotBlank(String string)
    {
        return !isBlank(string);
    }
}
