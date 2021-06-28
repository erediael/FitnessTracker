package be.fitnessTracker.bl;

import be.fitnessTracker.internal.AsyncUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;

@Component
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    public Mono<Void> sendForgotPasswordEmail(String email, String password) {
        String subject = "Forgot password email from FitnessTracker.com";
        String text = String.format("Use this password on your next login:\n\n%s", password);
        return sendSimpleMailMessage(email, subject, text);
    }

    private Mono<Void> sendSimpleMailMessage(String email, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject(subject);
        msg.setText(text);

        return AsyncUtil.runOnBoundedElasticScheduler(() -> {
            try {
                javaMailSender.send(msg);
            } catch (MailException ex) {
                logger.error("Email exception happen, either no such email to be sent to or smtp problems", ex);
            }
            return null;
        });
    }
}
