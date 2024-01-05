package com.saw.sns.email.validation;

import com.saw.sns.SnsApplicationTests;
import com.saw.sns.common.SnsResponse;
import com.saw.sns.email.model.vm.EmailVm;
import com.saw.sns.email.service.EmailServiceImpl;
import com.saw.sns.exception.ValidationErrorException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EmailServiceValidationTest extends SnsApplicationTests
{
    @Mock
    EmailServiceImpl emailService;
    @InjectMocks
    EmailServiceValidation emailServiceValidation;

    @Test
    public void testSendEmail_withValidEmail_shouldCallEmailServiceAndReturnSnsResponse() throws Exception {
        EmailVm email = EmailVm.builder()
                .to(new String[]{"test@xyz.com"})
                .from("no-reply@proto.me")
                .message("Test message")
                .build();

        SnsResponse expectedResponse = SnsResponse.builder()
                .message("Message sent")
                .build();
        when(emailService.sendEmail(email)).thenReturn(expectedResponse);

        SnsResponse actualResponse = emailServiceValidation.sendEmail(email);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testSendEmail_withInvalidToField_shouldThrowValidationErrorException() {
        EmailVm email = EmailVm.builder()
                .to(new String[]{"test@"})
                .from("no-reply@proto.me")
                .message("Test message")
                .build();

        ValidationErrorException e = assertThrows(ValidationErrorException.class, () -> emailServiceValidation.sendEmail(email));

        assertEquals(e.getErrors().get(0), "Invalid email address in to field: " + email.getTo()[0]);
    }

    @Test
    public void testSendEmail_withInvalidBccField_shouldThrowValidationErrorException() {
        EmailVm email = EmailVm.builder()
                .bcc(new String[]{"test@"})
                .from("no-reply@proto.me")
                .message("Test message")
                .build();

        ValidationErrorException e = assertThrows(ValidationErrorException.class, () -> emailServiceValidation.sendEmail(email));

        assertEquals(e.getErrors().get(0), "Invalid email address in bcc field: " + email.getBcc()[0]);
    }

    @Test
    public void testValidate_withMissingEmailBody_shouldAddError() {
        EmailVm email = EmailVm.builder()
                .to(new String[]{"test@xyz.com"})
                .from("no-reply@proto.me")
                .message("Test message")
                .build();

        ValidationErrorException e = assertThrows(ValidationErrorException.class, () -> emailServiceValidation.sendEmail(null));

        assertEquals(e.getErrors().get(0), "from, to/bcc and message fields are required");
    }

    @Test
    public void testValidate_withMissingAttachmentWhenFilenameIsSet_shouldAddError() {
        EmailVm email = EmailVm.builder()
                .to(new String[]{"test@xyz.com"})
                .from("no-reply@proto.me")
                .message("Test message")
                .fileName("test.pdf")
                .build();

        ValidationErrorException e = assertThrows(ValidationErrorException.class, () -> emailServiceValidation.sendEmail(email));

        assertEquals(e.getErrors().get(0), "attachment field cannot be blank or empty");
    }

    @Test
    public void testValidate_withMissingFilenameWhenAttachmentIsSet_shouldAddError() {
        EmailVm email = EmailVm.builder()
                .to(new String[]{"test@xyz.com"})
                .from("no-reply@proto.me")
                .message("Test message")
                .attachment("iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAApgAAAKYB3X3/OAAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAANCSURBVEiJtZZPbBtFFMZ/M7ubXdtdb1xSFyeilBapySVU8h8OoFaooFSqiihIVIpQBKci6KEg9Q6H9kovIHoCIVQJJCKE1ENFjnAgcaSGC6rEnxBwA04Tx43t2FnvDAfjkNibxgHxnWb2e/u992bee7tCa00YFsffekFY+nUzFtjW0LrvjRXrCDIAaPLlW0nHL0SsZtVoaF98mLrx3pdhOqLtYPHChahZcYYO7KvPFxvRl5XPp1sN3adWiD1ZAqD6XYK1b/dvE5IWryTt2udLFedwc1+9kLp+vbbpoDh+6TklxBeAi9TL0taeWpdmZzQDry0AcO+jQ12RyohqqoYoo8RDwJrU+qXkjWtfi8Xxt58BdQuwQs9qC/afLwCw8tnQbqYAPsgxE1S6F3EAIXux2oQFKm0ihMsOF71dHYx+f3NND68ghCu1YIoePPQN1pGRABkJ6Bus96CutRZMydTl+TvuiRW1m3n0eDl0vRPcEysqdXn+jsQPsrHMquGeXEaY4Yk4wxWcY5V/9scqOMOVUFthatyTy8QyqwZ+kDURKoMWxNKr2EeqVKcTNOajqKoBgOE28U4tdQl5p5bwCw7BWquaZSzAPlwjlithJtp3pTImSqQRrb2Z8PHGigD4RZuNX6JYj6wj7O4TFLbCO/Mn/m8R+h6rYSUb3ekokRY6f/YukArN979jcW+V/S8g0eT/N3VN3kTqWbQ428m9/8k0P/1aIhF36PccEl6EhOcAUCrXKZXXWS3XKd2vc/TRBG9O5ELC17MmWubD2nKhUKZa26Ba2+D3P+4/MNCFwg59oWVeYhkzgN/JDR8deKBoD7Y+ljEjGZ0sosXVTvbc6RHirr2reNy1OXd6pJsQ+gqjk8VWFYmHrwBzW/n+uMPFiRwHB2I7ih8ciHFxIkd/3Omk5tCDV1t+2nNu5sxxpDFNx+huNhVT3/zMDz8usXC3ddaHBj1GHj/As08fwTS7Kt1HBTmyN29vdwAw+/wbwLVOJ3uAD1wi/dUH7Qei66PfyuRj4Ik9is+hglfbkbfR3cnZm7chlUWLdwmprtCohX4HUtlOcQjLYCu+fzGJH2QRKvP3UNz8bWk1qMxjGTOMThZ3kvgLI5AzFfo379UAAAAASUVORK5CYII=")
                .build();

        ValidationErrorException e = assertThrows(ValidationErrorException.class, () -> emailServiceValidation.sendEmail(email));

        assertEquals(e.getErrors().get(0), "filename field cannot be blank or empty");
    }
}