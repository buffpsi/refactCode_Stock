package com.ll.tenmindaily.boundedContext.board.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findByInvestment(String investmentType);
}
