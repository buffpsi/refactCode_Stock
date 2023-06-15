package com.ll.tenmindaily.boundedContext.board.category;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCategory is a Querydsl query type for Category
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategory extends EntityPathBase<Category> {

    private static final long serialVersionUID = 1769059061L;

    public static final QCategory category = new QCategory("category");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath investment = createString("investment");

    public final ListPath<com.ll.tenmindaily.boundedContext.board.question.Question, com.ll.tenmindaily.boundedContext.board.question.QQuestion> questions = this.<com.ll.tenmindaily.boundedContext.board.question.Question, com.ll.tenmindaily.boundedContext.board.question.QQuestion>createList("questions", com.ll.tenmindaily.boundedContext.board.question.Question.class, com.ll.tenmindaily.boundedContext.board.question.QQuestion.class, PathInits.DIRECT2);

    public QCategory(String variable) {
        super(Category.class, forVariable(variable));
    }

    public QCategory(Path<? extends Category> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategory(PathMetadata metadata) {
        super(Category.class, metadata);
    }

}

