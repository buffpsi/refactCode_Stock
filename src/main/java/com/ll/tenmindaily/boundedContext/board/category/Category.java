package com.ll.tenmindaily.boundedContext.board.category;


import com.ll.tenmindaily.boundedContext.board.question.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String investment;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Question> questions = new ArrayList<>();

}
