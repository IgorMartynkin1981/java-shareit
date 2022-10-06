package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.item.Item;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;


@Entity
@Data
@Table(name = "requests")
public class ItemRequest {

    @Id
    @Column(name = "request_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(nullable = false)
    private String description;
    @Column(name = "creation_time", nullable = false)
    private LocalDateTime creationTime;
    @OneToMany
    @JoinColumn(name = "request_id")
    private Collection<Item> items;

    public ItemRequest() {

    }

    public ItemRequest(Long userId, String description, LocalDateTime creationTime) {
        this.userId = userId;
        this.description = description;
        this.creationTime = creationTime;
    }
}
