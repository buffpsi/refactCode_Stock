package com.ll.tenmindaily.boundedContext.member.memberdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ll.tenmindaily.boundedContext.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    @JsonProperty("id")
    private  Long id;
    @NotBlank
    private String userId;
    @NotBlank
    private  String username;
    @NotBlank
    private  String email;
    @NotBlank
    private  String nickname;

    public static MemberDto fromUser(Member user) {
        MemberDto memberDto = MemberDto.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .username(user.getUsername())
                .build();

        return memberDto;
    }

}
