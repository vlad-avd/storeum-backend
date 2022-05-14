package com.storeum.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
    @JsonIgnore
    private Folder folder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @ManyToMany
    @JoinTable(name = "note_tags",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonIncludeProperties({"id", "title"})
    private Set<Tag> tags;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", tags=[" +
                (tags != null
                        ? tags.stream().map(Tag::getTitle).collect(Collectors.joining(", "))
                        : ""
                ) + "]" +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
