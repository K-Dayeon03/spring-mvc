package com.codeit.mvc.dto.reponse;

import com.codeit.mvc.domain.Category;
import com.codeit.mvc.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// record: java 16부터 정식 도입된 클래스 문법, 불변 데이터 객체를 위해 특화시켜 만든 새로운 클래스
// 1. 모든 필드를 private final로 만들어 줍니다.
// 2. getter 메서드가 자동 생성됩니다. record가 제공하는 getter는 필드명과 메서드명이 똑같습니다. (get이 안붙음!)
// 3. 생성자, equals(), hashCode(), toString()이 자동 구현됩니다. Lombok을 쓸 필요가 없습니다.
// 4. Builder 사용은 지양하는 편입니다. (생성자가 기본으로 제공되므로 생성자를 쓰는 것을 선호합니다.)
public record PostResponse(
        Long id,
        String title,
        String content,
        String author,
        Category category,
        int viewCount,
        String thumbnailPath,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getCategory(),
                post.getViewCount(),
                post.getThumbnailPath(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}







