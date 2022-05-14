package com.storeum.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "folder")
@Getter
@Setter
@NoArgsConstructor
@NamedEntityGraphs(
        @NamedEntityGraph(name = "entireFolderTree",
                attributeNodes = {
                        @NamedAttributeNode(value = "subFolders", subgraph = "childFolders"),
                        @NamedAttributeNode(value = "tags"),
                },
                subgraphs = @NamedSubgraph(name = "childFolders",
                        attributeNodes = {
                                @NamedAttributeNode("subFolders"),
                                @NamedAttributeNode("tags"),
                        })
        )
)
public class Folder {

    @Id
    @SequenceGenerator(name = "f_seq", sequenceName = "folder_sequence", allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "f_seq")
    private Long id;

    private String title;

    @ManyToOne
    @JsonIncludeProperties({"id"})
    private Folder parentFolder;

    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Folder> subFolders = new HashSet<>();

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIncludeProperties({"id", "title"})
    private Set<Tag> tags;
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
