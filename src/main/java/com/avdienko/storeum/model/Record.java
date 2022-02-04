package com.avdienko.storeum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "record")
@Getter
@Setter
@NoArgsConstructor
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 30)
    private String title;

    private String description;

    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIncludeProperties({"id"})
    private Folder folder;

    @ManyToOne
    @JsonIgnore
    private User user;
}
