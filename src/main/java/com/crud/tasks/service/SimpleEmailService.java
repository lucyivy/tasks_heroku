package com.crud.tasks.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.crud.tasks.domain.Mail;
import com.crud.tasks.scheduler.EmailScheduler;

@Service
public class SimpleEmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMailMessage.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailCreatorService mailCreatorService;

    public void send(final Mail mail) {
        LOGGER.info("Starting e-mail preparation");

        try {
            // SimpleMailMessage mailMessage = createMailMessage(mail);
            // javaMailSender.send(mailMessage);
            javaMailSender.send(createMimeMessage(mail));
            LOGGER.info("E-mail sent");
        } catch (MailException e) {
            LOGGER.error("Failed to process the e-mail sending: ", e.getMessage(), e);
        }
    }

    private MimeMessagePreparator createMimeMessage (final Mail mail){
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(mail.getMailTo());
            messageHelper.setSubject(mail.getSubject());
            if(mail.getSubject().equals(TrelloService.SUBJECT)) {
                messageHelper.setText(mailCreatorService.buildTrelloCardEmail
                        (mail.getMessage()), true);
            } else if (mail.getSubject().equals(EmailScheduler.SUBJECT)) {
                messageHelper.setText(mailCreatorService.buildTaskAmountEmail(mail.getMessage()), true);
            } else {
                messageHelper.setText(mail.getMessage(), false);
            }
        };
    }

    private SimpleMailMessage createMailMessage(final Mail mail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mail.getMailTo());
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getMessage());
        if (mail.getToCc() != null) {
            mailMessage.setCc(mail.getToCc());
        }
        return mailMessage;
    }
}