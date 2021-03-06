package com.crud.tasks.scheduler;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.Mail;
import com.crud.tasks.repository.TaskRepository;
import com.crud.tasks.service.SimpleEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailScheduler {
    public static final String SUBJECT = "Tasks: Once a day e-mail";
    private String taskOrTasks;

    @Autowired
    private SimpleEmailService simpleEmailService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AdminConfig adminConfig;

    @Scheduled(cron = "0 0 17 * * *")
    public void sendInformationEmail() {
        long size = taskRepository.count();

        if (size == 1) {
            taskOrTasks = "task";
        } else {
            taskOrTasks = "tasks";
        }

        simpleEmailService.send(new Mail(
                adminConfig.getAdminMail(), SUBJECT, "Currently in database you got: " + size + " "
                + taskOrTasks));
    }
}
