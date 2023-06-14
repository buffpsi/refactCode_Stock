package com.ll.tenmindaily.boundedContext.board.question;



import com.ll.tenmindaily.base.exception.DataNotFoundException;
import com.ll.tenmindaily.boundedContext.board.answer.Answer;
import com.ll.tenmindaily.boundedContext.board.category.Category;
import com.ll.tenmindaily.boundedContext.member.entity.Member;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;


    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>(); //정렬을 위한 리스트
        sorts.add(Sort.Order.desc("createDate")); //역순 정렬

        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        //page 조회할 페이지의 번호, 10:한 페이지에 보일게시물의 갯수
        //역순으로 조회하기 위해서는 PageRequest.of 메서드의 세번째 파라미터로 Sort 객체를 전달
        Specification<Question> spec = search(kw);
        return this.questionRepository.findAll(spec, pageable);
        //return this.questionRepository.findAllByKeyword(kw, pageable);
    }

    public Page<Question> getList(int page, String kw, String type) {
        Page<Question> questionPage = getList(page, kw);
        if (!type.isEmpty()) {
            questionPage = new PageImpl<>(questionPage
                    .stream()
                    .filter(q -> q.getCategory().getInvestment().equals(type))
                    .collect(Collectors.toList()));
        }
        return questionPage;
    }

    public Question getQuestion(Integer id){
        Optional<Question> question = this.questionRepository.findById(id);
        if(question.isPresent()){
            Question question1 = question.get();
            question1.setView(question1.getView()+1);
            this.questionRepository.save(question1);
            return question1;
        }else{
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String subject, String content, Member user, Category category){
        Question q= new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        q.setCategory(category);
        this.questionRepository.save(q);
    }

    public void modify(Question question, String subject, String content, Category category){
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        question.setCategory(category);
        this.questionRepository.save(question);
    }

    public void delete(Question question){
        this.questionRepository.delete(question);
    }

    public void vote(Question question, Member member){ // ---- 유저 객체 구현후 추후 수정 --------------------
        question.getVoter().add(member);
        this.questionRepository.save(question);
    }

    //검색어(kw)를 입력받아 쿼리의 조인문과 where문을 생성하여 리턴하는 메서드
    //여러 테이블에서 데이터를 검색해야 할 경우에는
    // JPA가 제공하는 Specification 인터페이스를 사용
    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Question, Member> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, Member> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),    // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자  ---- 유저 객체 구현후 추후 수정 --------------------
            }
        };
    }




}
