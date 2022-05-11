package com.storeum.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "folder")
@Getter
@Setter
@NoArgsConstructor
public class Folder {

    @Id
    @SequenceGenerator(name = "f_seq", sequenceName = "folder_sequence", allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "f_seq")
    private Long id;

    private String title;

    @ManyToOne
    @JsonIncludeProperties({"id"})
//    @JsonIgnore
    private Folder parentFolder;

    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Folder> subFolders = new ArrayList<>();

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIncludeProperties({"id", "title"})
    private List<Tag> tags;
    @ManyToOne
    @JsonIgnore
    private User user;

    @Override
    public String toString() {
        return "Folder{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", tags=[" + tags.stream().map(Tag::getTitle).collect(Collectors.joining(", ")) + "]" +
                '}';
    }
}
