package com.avdienko.storeum.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "folder")
@Getter
@Setter
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//@ToString
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 30)
    private String title;

    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL, orphanRemoval = true)
//    @ToString.Exclude
//    @JsonBackReference
    private List<Folder> subFolders = new ArrayList<>();

    @ManyToOne
//    @JsonManagedReference
    @JsonIgnore
    private Folder parentFolder;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
//    @ToString.Exclude
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
//    @ToString.Exclude
    private List<Record> records = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @ToString.Exclude
    private User user;

    public Folder(String title, User user) {
        this.title = title;
        this.user = user;
    }
}
