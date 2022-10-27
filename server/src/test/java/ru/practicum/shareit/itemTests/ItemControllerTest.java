package ru.practicum.shareit.itemTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ObjectsForTests;
import ru.practicum.shareit.item.controllers.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.InfoCommentDto;
import ru.practicum.shareit.item.dto.InfoItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.services.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mvc;

    @Test
    void createItemTests() throws Exception {
        ItemDto itemDto = ObjectsForTests.getItemDto1();
        InfoItemDto infoItemDto = ObjectsForTests.getInfoItemDto1();
        when(itemService.createItem(any(), any())).thenReturn(infoItemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(infoItemDto)));
    }

    @Test
    void updateItemTests() throws Exception {
        ItemDto itemDto = ObjectsForTests.getItemDto1();
        InfoItemDto infoItemDto = ObjectsForTests.getInfoItemDto1();
        when(itemService.updateItem(any(), any(), any())).thenReturn(infoItemDto);

        mvc.perform(patch("/items/{itemId}", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(infoItemDto)));
    }

    @Test
    void findItemByIdTest() throws Exception {
        ItemDto itemDto = ObjectsForTests.getItemDto1();
        InfoItemDto infoItemDto = ObjectsForTests.getInfoItemDto1();
        when(itemService.findItemById(any(), any())).thenReturn(infoItemDto);

        mvc.perform(get("/items/{itemId}", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(infoItemDto)));
    }

    @Test
    void findAllItemsByOwnerIdTests() throws Exception {
        InfoItemDto infoItemDto = ObjectsForTests.getInfoItemDto1();
        when(itemService.findAllItemsByOwnerId(any(), any(), any())).thenReturn(List.of(infoItemDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(infoItemDto))));
    }

    @Test
    void searchItemsTests() throws Exception {
        InfoItemDto infoItemDto = ObjectsForTests.getInfoItemDto1();
        when(itemService.searchItemsByText(any(), any(), any())).thenReturn(List.of(infoItemDto));

        mvc.perform(get("/items/search")
                        .param("text", "кувалда")
                        .param("from", "0")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(infoItemDto))));
    }

    @Test
    void createCommentTests() throws Exception {
        InfoCommentDto infoCommentDto = ObjectsForTests.infoCommentDto();
        CommentDto commentDto = ObjectsForTests.commentDto();
        when(itemService.createComment(any(), any(), any())).thenReturn(infoCommentDto);

        mvc.perform(post("/items/{itemId}/comment", 1)
                        .content(mapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(infoCommentDto)));
    }
}