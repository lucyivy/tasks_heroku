package com.crud.tasks.service;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.Task;
import com.crud.tasks.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;



@Service
public class MailCreatorService {

    @Autowired
    private AdminConfig adminConfig;

    @Autowired
    @Qualifier("templateEngine")
    private TemplateEngine templateEngine;

    @Autowired
    private TaskRepository repository;




    public String buildTrelloCardEmail(String message){
        List<String> functionality = new ArrayList<String>();
        functionality.add("You can manage your tasks");
        functionality.add("Provides connection with Trello Account");
        functionality.add("Application allows sending tasks to Trello");
        Context context = new Context();
        context.setVariable("message", message);
        context.setVariable("tasks_url", "http://localhost:8888/crud");
        context.setVariable("button", "Visit website");
        context.setVariable("show_button", false);
        context.setVariable("admin_name", adminConfig.getAdminName());
        context.setVariable("is_friend", true);
        context.setVariable("goodbye_message", "Best regards, \nTask Application Team");
        context.setVariable("preview_message", "New Trello card created");
        context.setVariable("company_details", "Company: " + adminConfig.getCompanyName()
                + ", e-mail: " + adminConfig.getCompanyMail());
        context.setVariable("admin_config", adminConfig);
        context.setVariable("application_functionality", functionality);
        return templateEngine.process("mail/created-trello-card-name", context);
    }

    public String buildTaskAmountEmail(String message){
        List<String> taskNames = new ArrayList<String>();
        for(Task task :  repository.findAll()) {
            taskNames.add(task.getTitle());
        }
        Context context = new Context();
        context.setVariable("message", message);
        context.setVariable("tasks_url", "http://localhost:8888/crud");
        context.setVariable("button", "Visit website");
        context.setVariable("show_button", false);
        context.setVariable("is_friend", true);
        context.setVariable("goodbye_message", "Best regards, \nTask Application Team");
        context.setVariable("preview_message", "Current amount of your tasks");
        context.setVariable("admin_config", adminConfig);
        context.setVariable("task_names", taskNames);
        return templateEngine.process("mail/task-amount", context);
    }
}