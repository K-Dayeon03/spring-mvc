package com.codeit.mvc.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Comment {

    @Setter
    private Long id;
    private Long postId;
    private String content;
    private String author;
    private LocalDateTime createdAt;

    public Comment(){
        this.createdAt = LocalDateTime.now();
    }


    @Builder
    public Comment(Long postId, String content, String author) {
        this();
        this.postId = postId;
        this.content = content;
        this.author = author;
    }
}
