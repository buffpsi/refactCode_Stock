package com.ll.tenmindaily.boundedContext.board.answer;



import com.ll.tenmindaily.boundedContext.board.comment.Comment;
import com.ll.tenmindaily.boundedContext.board.question.Question;
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
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne //답변은 하나의 질문에 여러개가 달릴 수 있는 구조, 앞쪽이 '나' 이다.
    private Question question;

    @ManyToOne
    private Member author;

    @ManyToMany //새로운 테이블을 생성
    Set<Member> voter;

    @OneToMany(mappedBy = "answer")
    private List<Comment> commentList;
}
