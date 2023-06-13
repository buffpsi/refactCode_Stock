package com.ll.tenmindaily.boundedContext.member.repository;

import com.ll.tenmindaily.boundedContext.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByUserId(String userId);

    Optional<Member> findByUserIdAndEmail(String userId, String email);

    Optional<Member> findByEmail(String email);
}
