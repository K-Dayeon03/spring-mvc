package com.codeit.mvc.dto.reponse;

import com.codeit.mvc.domain.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        String author,
        LocalDateTime createdAt
) {

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor(),
                comment.getCreatedAt()
        );
    }

}