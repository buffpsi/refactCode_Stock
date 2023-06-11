package com.ll.tenmindaily.boundedContext.emailVerification.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEmailVerification is a Querydsl query type for EmailVerification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmailVerification extends EntityPathBase<EmailVerification> {

    private static final long serialVersionUID = 2028168308L;

    public static final QEmailVerification emailVerification = new QEmailVerification("emailVerification");

    public final StringPath code = createString("code");

    public final DateTimePath<java.time.LocalDateTime> expireDate = createDateTime("expireDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public QEmailVerification(String variable) {
        super(EmailVerification.class, forVariable(variable));
    }

    public QEmailVerification(Path<? extends EmailVerification> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEmailVerification(PathMetadata metadata) {
        super(EmailVerification.class, metadata);
    }

}

