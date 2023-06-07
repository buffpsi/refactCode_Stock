package com.ll.tenmindaily.boundedContext.board.question;



import com.ll.tenmindaily.boundedContext.board.answer.Answer;
import com.ll.tenmindaily.boundedContext.board.category.Category;
import com.ll.tenmindaily.boundedContext.board.comment.Comment;
import com.ll.tenmindaily.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //값을 따로 세팅하지 않아도 1씩 자동으로 증가
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)// 참조 엔티티의 속성명,답변들도 모두 함께 삭제
    private List<Answer> answerList;

    @ManyToOne
    private Member author; //---- 유저 객체 구현후 추후 수정 --------------------

    @ManyToMany
    Set<Member> voter; //---- 유저 객체 구현후 추후 수정 --------------------

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;
    // Comment 모델에서 Question을 연결하기 위한 속성명이 question이므로
    // mappedBy의 값으로 "question"이 전달

    @ManyToOne
    Category category;
}
