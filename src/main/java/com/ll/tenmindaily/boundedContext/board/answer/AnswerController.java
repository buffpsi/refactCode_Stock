package com.ll.tenmindaily.boundedContext.board.answer;



import com.ll.tenmindaily.boundedContext.board.category.Category;
import com.ll.tenmindaily.boundedContext.board.question.Question;
import com.ll.tenmindaily.boundedContext.board.question.QuestionForm;
import com.ll.tenmindaily.boundedContext.board.question.QuestionService;
import com.ll.tenmindaily.boundedContext.member.entity.Member;
import com.ll.tenmindaily.boundedContext.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final MemberService memberService;

    /*//매개변수로 바인딩한 객체는 Model 객체로 전달하지 않아도 템플릿에서 사용이 가능=QuestionForm
    @PreAuthorize("isAuthenticated()") //로그인이 필요한 메서드
    @GetMapping("/create")
    public String questionCreate(Model model, QuestionForm questionForm){
        model.addAttribute("categoryList", categoryService.getinvestmentType());
        return "usr/board/question_form";
    }

    //QuestionForm 이 만들어 지면서 유효성 검증을 함. subject, content 는 questionForm 에 들어있음
    // 자동으로 바인딩 된다
    // @Valid = @NotEmpty, @Size 등으로 설정한 검증 기능이 동작
    // bindingResult =  검증이 수행된 결과
    //BindingResult 매개변수는 항상 @Valid 매개변수 바로 뒤에 위치
    @PreAuthorize("isAuthenticated()") //로그인이 필요한 메서드
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            return "usr/board/question_form";
        }
        Member member = this.memberService.getUser(principal.getName()); //---- 유저 객체 구현후 추후 수정 --------------------
        Category category = this.categoryService.getCategory(questionForm.getCategory());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), member, category);//--- 유저 객체 구현후 추후 수정 --------------------
        return "redirect:/question/list";
    }*/




    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create/{id}")
    public String createAnswer(AnswerForm answerForm, @PathVariable("id") Integer id, Model model){
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "usr/board/answer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id,
                               @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal){
        Question question = this.questionService.getQuestion(id);
        Member member = this.memberService.getUser(principal.getName());
        if(bindingResult.hasErrors()){
            model.addAttribute("question", question);
            return "usr/board/question_detail";
        }
        Answer answer = this.answerService.craete(question, answerForm.getContent(), member);
        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal){
        //답변 수정시 기존의 내용이 필요
        Answer answer = this.answerService.getAnswer(id);
        if(!answer.getAuthor().getUserId().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }//-------현제 사용자랑 답변 저자랑 비교하는 메소드------유저 객체 구현후 추후 수정 --------------------
        answerForm.setContent(answer.getContent());
        return "usr/board/answer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal){
        if(bindingResult.hasErrors()){
            return "usr/board/answer_form";
        }
        Answer answer = this.answerService.getAnswer(id);
        if(!answer.getAuthor().getUserId().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }//-------현제 사용자랑 답변 저자랑 비교하는 메소드------ 유저 객체 구현후 추후 수정 --------------------
        this.answerService.modify(answer, answerForm.getContent());
        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id){
        Answer answer = this.answerService.getAnswer(id);
        if(!answer.getAuthor().getUserId().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }//-------현제 사용자랑 답변 저자랑 비교하는 메소드------ 유저 객체 구현후 추후 수정 --------------------
        this.answerService.delete(answer);
        return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(Principal principal, @PathVariable("id") Integer id){
        Answer answer = this.answerService.getAnswer(id);
        Member member = this.memberService.getUser(principal.getName());
        this.answerService.vote(answer, member);//------ 유저 객체 구현후 추후 수정 ----------
        return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
    }
}
