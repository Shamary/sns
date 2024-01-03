package com.saw.sns.email.validation;

import com.saw.sns.common.SnsResponse;
import com.saw.sns.email.infc.EmailService;
import com.saw.sns.email.model.vm.EmailVm;
import com.saw.sns.email.service.EmailServiceImpl;
import com.saw.sns.exception.OperationFailedException;
import com.saw.sns.exception.ValidationErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class EmailServiceValidation implements EmailService {
    @Autowired
    private EmailServiceImpl emailService;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    @Override
    public SnsResponse sendEmail(EmailVm email) throws ValidationErrorException, OperationFailedException {
        List<String> errors = validate(email);
        if (errors.size() > 0) {
            throw new ValidationErrorException(errors);
        }
        return emailService.sendEmail(email);
    }

    private List<String> validate(EmailVm emailVm)
    {
        List<String> errors = new ArrayList<>();

        if (emailVm == null) {
            errors.add("from, to and message fields are required");
            return errors;
        }

        if (isNullOrBlank(emailVm.getTo())) {
            errors.add("to field cannot be blank or empty");
        } else {
            for (String to : emailVm.getTo()) {
                if (!isValidEmailAddress(to)) {
                    errors.add("Invalid email address in to field: " + to);
                }
            }
        }

        if (StringUtils.isEmpty(emailVm.getFrom())) {
            errors.add("from field cannot be blank or empty");
        }

        if (StringUtils.isEmpty(emailVm.getMessage())) {
            errors.add("message field cannot be blank or empty");
        }

        validateEmailAddress(emailVm.getFrom(), "from", errors);
        validateEmailAddresses(emailVm.getCc(), "cc", errors);
        validateEmailAddresses(emailVm.getBc(), "bcc", errors);

        if (!StringUtils.isEmpty(emailVm.getFileName())) {
            if (StringUtils.isEmpty(emailVm.getAttachment())) {
                errors.add("attachment field cannot be blank or empty");
            }
            else if (!validateBase64(emailVm.getAttachment())) {
                errors.add("attachment field must be a valid base64 value");
            }
        }

        if (!StringUtils.isEmpty(emailVm.getAttachment())) {
            if (StringUtils.isEmpty(emailVm.getFileName())) {
                errors.add("filename field cannot be blank or empty");
            }
        }

        return errors;
    }
    private static void validateEmailAddresses(String[] emails, String fieldName, List<String> errors) {
        if (!isNullOrBlank(emails)) {
            for (String email : emails) {
                if (!isValidEmailAddress(email)) {
                    errors.add("Invalid email address in " + fieldName + " field: " + email);
                }
            }
        }
    }

    private static void validateEmailAddress(String email, String fieldName, List<String> errors) {
        if (!StringUtils.isEmpty(email)) {
            if (!isValidEmailAddress(email)) {
                errors.add("Invalid email address in " + fieldName + " field: " + email);
            }
        }
    }

    private static boolean isNullOrBlank(String[] array) {
        return array == null || array.length == 0 || array[0] == null || array[0].trim().isEmpty();
    }

    private static boolean isValidEmailAddress(String emailAddress) {
        return EMAIL_PATTERN.matcher(emailAddress).matches();
    }

    public boolean validateBase64(String base64) {
        try {
            Base64.getDecoder().decode(base64);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
