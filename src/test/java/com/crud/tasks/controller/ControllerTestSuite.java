package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
public class ControllerTestSuite {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskMapper mapper;

    @MockBean
    private DbService dbService;

    @Test
    public void shouldGetTasks() throws Exception {
        //Given
        List<TaskDto> taskList = new ArrayList<>();
        taskList.add(new TaskDto(1L, "title", "content"));

        when(mapper.mapToTaskDtoList(dbService.getAllTasks())).thenReturn(taskList);

        //When&Then
        mockMvc.perform(get("/v1/tasks").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("title")))
                .andExpect(jsonPath("$[0].content", is("content")));
    }

    @Test
    public void shouldGetTask() throws Exception {
        //Given
        when(mapper.maptoTaskDto(ArgumentMatchers.any())).thenReturn(new TaskDto(1L, "title", "content"));
        when(dbService.getTask(1L)).thenReturn(Optional.of(new Task(1L, "title", "content")));

        //When&Then
        mockMvc.perform(get("/v1/tasks/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("title")))
                .andExpect(jsonPath("$.content", is("content")));
    }

    @Test
    public void shouldDeleteTask() throws Exception {


        //When
        mockMvc.perform(delete("/v1/tasks/1").contentType(MediaType.APPLICATION_JSON));

        //Then
        verify(dbService, times(1)).deleteTask(ArgumentMatchers.any());
    }

    @Test
    public void shouldUpdateTask() throws Exception {
        //Given
        List<TaskDto> taskList = new ArrayList<>();
        taskList.add(new TaskDto(1L, "title", "content"));
        taskList.add(new TaskDto(2L, "title2", "content2"));


        when(mapper.maptoTaskDto(dbService.saveTask(mapper.mapToTask(taskList.get(0))))).thenReturn(taskList.get(0));
        Gson gson = new Gson();
        String jsonContent = gson.toJson(taskList.get(0));

        //When&Then
        mockMvc.perform(put("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("title")))
                .andExpect(jsonPath("$.content", is("content")));
    }

    @Test
    public void shouldCreateTask() throws Exception {
        //Given
        List<TaskDto> taskList = new ArrayList<>();
        taskList.add(new TaskDto(1L, "title", "content"));
        taskList.add(new TaskDto(2L, "title2", "content2"));

        Gson gson = new Gson();
        String jsonContent = gson.toJson(taskList.get(0));

        //When&Then
        mockMvc.perform(post("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk());
        verify(dbService, times(1)).saveTask(ArgumentMatchers.any());
    }

}
