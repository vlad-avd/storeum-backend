package com.storeum.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "note")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    @Id
    @SequenceGenerator(name = "n_seq", sequenceName = "note_sequence", allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "n_seq")
    private Long id;

    private String title;

    private String description;

    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIncludeProperties({"id"})
    private Folder folder;

    @ManyToOne
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIncludeProperties({"id", "title"})
    private List<Tag> tags;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
