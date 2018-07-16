package com.crud.tasks.domain;

import org.junit.Test;

import com.crud.tasks.domain.Mail;
import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;

import org.junit.Assert;


public class DtoTestSuite {


    @Test
    public void testTaskandTaskDto() {

        //When
        Task task = new Task((long) 1, "test name", "test description");
        TaskDto taskDto =  new TaskDto((long) 1, "test name", "test description");

        //Then
        Assert.assertEquals(task.getTitle(), taskDto.getTitle());
        Assert.assertEquals(task.getId(), taskDto.getId());
        Assert.assertEquals(task.getContent(), taskDto.getContent());
    }

    @Test
    public void testMail() {

        //Given & When
        Mail mail = new Mail("testMailTo", "TestSubject", "TestMessage");

        //Then
        Assert.assertNull(mail.getToCc());
        Assert.assertEquals("testMailTo",  mail.getMailTo());
        Assert.assertEquals("TestSubject",  mail.getSubject());
        Assert.assertEquals("TestMessage", mail.getMessage());
    }
}
