package ru.practicum.shareit.bookingTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ObjectsForTests;
import ru.practicum.shareit.booking.controllers.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InfoBookingDto;
import ru.practicum.shareit.booking.services.BookingServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingServiceImpl bookingService;
    @Autowired
    private MockMvc mvc;

    @Test
    void createBooking() throws Exception {
        BookingDto bookingDto = ObjectsForTests.futureBookingDto1();
        InfoBookingDto infoBookingDto = ObjectsForTests.futureInfoBookingDTO();
        when(bookingService.createBooking(any(), any())).thenReturn(infoBookingDto);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(infoBookingDto)));
    }

    @Test
    void approveBooking() throws Exception {
        InfoBookingDto infoBookingDto = ObjectsForTests.futureInfoBookingDTO();
        when(bookingService.approveBooking(any(), any(), any())).thenReturn(infoBookingDto);
        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", "1")
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(infoBookingDto)));
    }

    @Test
    void getBookingById() throws Exception {
        InfoBookingDto infoBookingDto = ObjectsForTests.futureInfoBookingDTO();
        when(bookingService.findBookingById(any(), any())).thenReturn(infoBookingDto);
        mvc.perform(get("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(infoBookingDto)));
    }

    @Test
    void getBookingsByUserId() throws Exception {
        List<InfoBookingDto> bookings = List.of(ObjectsForTests.futureInfoBookingDTO());
        when(bookingService.findAllBookingsByUserId(any(), any(), any(), any())).thenReturn(bookings);
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL")
                        .param("from", "1")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookings)));
    }

    @Test
    void getBookingsByOwnerId() throws Exception {
        List<InfoBookingDto> bookings = List.of(ObjectsForTests.futureInfoBookingDTO());
        when(bookingService.findAllBookingsByOwnerId(any(), any(), any(), any())).thenReturn(bookings);
        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL")
                        .param("from", "1")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookings)));
    }
}