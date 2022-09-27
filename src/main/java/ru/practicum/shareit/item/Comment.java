package ru.practicum.shareit.item;

import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @Column(name = "comment_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "comment_text", nullable = false)
    String text;
    @Column(name = "item_id", nullable = false)
    Long itemId;
    @ManyToOne
    @JoinColumn(name = "author_id")
    User author;
    @Column(name = "created", nullable = false)
    LocalDateTime created;

    public Comment(String text, Long itemId, User author, LocalDateTime created) {
        this.text = text;
        this.itemId = itemId;
        this.author = author;
        this.created = created;
    }

    public Comment() {
    }
}
