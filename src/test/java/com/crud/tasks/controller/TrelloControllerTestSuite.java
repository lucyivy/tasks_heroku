package com.crud.tasks.controller;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import com.crud.tasks.service.TrelloService;
import org.mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.domain.TrelloListDto;
import com.crud.tasks.domain.*;
import com.google.gson.Gson;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@WebMvcTest(TrelloController.class)
public class TrelloControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrelloService trelloService;

    @Test
    public void shouldFetchEmptyTrelloBoards() throws Exception {
        //Given
        List<TrelloBoardDto> trelloBoards = new ArrayList<>();
        when(trelloService.fetchTrelloBoards()).thenReturn(trelloBoards);
        //When&Then
        mockMvc.perform(get("/v1/trello/boards").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200)) //or isOk
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void shouldFetchTrelloBoards() throws Exception {
        //Given
        List<TrelloListDto> trelloLists = new ArrayList<>();
        trelloLists.add(new TrelloListDto("Test List", "Test List", false));

        List<TrelloBoardDto> trelloBoards = new ArrayList<>();
        trelloBoards.add(new TrelloBoardDto("test", "test", trelloLists));
        when(trelloService.fetchTrelloBoards()).thenReturn(trelloBoards);


        //When & Then
        mockMvc.perform(get("/v1/trello/boards").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //TrelloBoard fields
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("test")))
                .andExpect(jsonPath("$[0].name", is("test")))
                //TrelloListfields
                .andExpect(jsonPath("$[0].lists", hasSize(1)))
                .andExpect(jsonPath("$[0].lists[0].id", is("Test List")))
                .andExpect(jsonPath("$[0].lists[0].name", is("Test List")))
                .andExpect(jsonPath("$[0].lists[0].closed", is(false)));
    }

    @Test
    public void shouldCreateTrelloCard() throws Exception {
        //Given
        TrelloCardDto trelloCardDto = new TrelloCardDto("name", "description", "top", "1");

        CreatedTrelloCard createdTrelloCardDto = new CreatedTrelloCard("1", "name", "testUrl");

        when(trelloService.createTrelloCard(ArgumentMatchers.any())).thenReturn(createdTrelloCardDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(trelloCardDto);

        //When&Then
        mockMvc.perform(post("/v1/trello/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.shortUrl", is("testUrl")));
    }
}
