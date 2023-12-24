package com.saw.sns.sms.service;

import com.saw.sns.exception.OperationFailedException;
import com.saw.sns.exception.ValidationErrorException;
import com.saw.sns.sms.infc.SmsService;
import com.saw.sns.sms.model.vm.SmsVm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

@Service
public class SmsServiceImpl implements SmsService {
    Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);
    @Override
    public String SendMessage(SmsVm smsVm) throws ValidationErrorException, OperationFailedException {
        SnsClient client = SnsClient.builder().region(Region.US_EAST_1).build();
        for (String number : smsVm.getTo()) {
            try
            {

                PublishRequest request = PublishRequest.builder()
                        .message(smsVm.getMessage())
                        .phoneNumber(number)
                        .build();

                PublishResponse response = client.publish(request);
                logger.debug("======SMS RESPONSE " + response);
            }
            catch (Exception e) {
                client.close();
                logger.error("======SMS ERROR " + e.getMessage());
                throw new OperationFailedException("Sending SMS failed\n " + e.getMessage());
            }
        }
        client.close();
        return "Message sent";
    }
}
