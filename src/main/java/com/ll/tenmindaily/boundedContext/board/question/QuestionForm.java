package com.ll.tenmindaily.boundedContext.board.question;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

//화면에서 전달되는 입력 값을 검증하기 위한 클래스
// 자동으로 바인딩 된다
@Getter
@Setter
public class QuestionForm {

    @NotEmpty(message = "제목은 필수항목 입니다")
    @Size(max = 200)
    private String subject;

    @NotEmpty(message = "내용은 필수항목 입니다.")
    private String content;

    @NotEmpty(message = "카테고리는 필수항목 입니다.")
    private String category;
}
