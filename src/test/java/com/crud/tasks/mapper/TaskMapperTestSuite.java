package com.crud.tasks.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;

import org.junit.Assert;

@RunWith(MockitoJUnitRunner.class)
public class TaskMapperTestSuite {

    @InjectMocks
    private TaskMapper mapper;

    @Test
    public void testMapToTaskDto() {
        //Given
        Task task = new Task((long) 1, "test name", "test description");

        //When
        TaskDto result = mapper.maptoTaskDto(task);

        //Then
        Assert.assertEquals((Long) 1L, result.getId());
        Assert.assertEquals("test name",  result.getTitle());
        Assert.assertEquals("test description", result.getContent());
    }

    @Test
    public void testMapToTask() {
        //Given
        TaskDto taskDto =  new TaskDto(1L, "test name", "test description");

        //When
        Task result = mapper.mapToTask(taskDto);

        //Then
        Assert.assertEquals((Long) (long) 1, result.getId());
        Assert.assertEquals("test name",  result.getTitle());
        Assert.assertEquals("test description", result.getContent());
    }

}