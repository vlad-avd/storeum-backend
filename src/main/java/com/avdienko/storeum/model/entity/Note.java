package com.avdienko.storeum.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

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

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
