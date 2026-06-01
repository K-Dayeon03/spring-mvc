package com.codeit.mvc.dto.response;

import com.codeit.mvc.domain.Category;
import com.codeit.mvc.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// 응답용 DTO: 응답 과정에서도 Entity를 직접 리턴하는 것은 화면단에서 요구하는 데이터와 일치하지 않을 수 있다.
// (너무 과하거나 부족한 경우도 생길 수 있다.)
// 각 응답에 맞는 DTO를 제작해서 응답을 주자.
@Getter
public class PostResponse {

    // DTO의 필드가 Entity와 동일하긴 하지만, 그래도 DTO로 변환해서 내리는 것을 추천
    // 예를 들어 날짜를 가공해서 응답하거나, 이미지 경로 등을 가공해서 내려야 할 필요성도 있습니다.
    private Long id;
    private String title;
    private String content;
    private String author;
    private Category category;
    private int viewCount;
    private String thumbnailPath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 1. 외부에서 직접 객체를 생성하지 못하도록 기본 생성자를 private으로 막자.
    private PostResponse() {}

    // 2. Builder 객체를 받아 필드를 초기화하는 private 생성자를 만들자.
    private PostResponse(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.content = builder.content;
        this.author = builder.author;
        this.category = builder.category;
        this.viewCount = builder.viewCount;
        this.thumbnailPath = builder.thumbnailPath;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    // 3. static 내부 클래스로 Builder 정의하기
    public static class Builder {
        // 원본 객체와 필드를 동일하게 세팅
        private Long id;
        private String title;
        private String content;
        private String author;
        private Category category;
        private int viewCount;
        private String thumbnailPath;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        // 4. 각 필드의 값을 세팅하고, Builder 자기 자신을 리턴하는 메서드들을 만듭니다.
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder title(String title) {
            this.title = title;
            return this;
        }
        public Builder content(String content) {
            this.content = content;
            return this;
        }
        public Builder author(String author) {
            this.author = author;
            return this;
        }
        public Builder category(Category category) {
            this.category = category;
            return this;
        }
        public Builder viewCount(int viewCount) {
            this.viewCount = viewCount;
            return this;
        }
        public Builder thumbnailPath(String thumbnailPath) {
            this.thumbnailPath = thumbnailPath;
            return this;
        }
        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        // 5. 최종적으로 원본 객체를 생성하여 반환하는 build() 메서드를 만듭니다.
        public PostResponse build() {
            return new PostResponse(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }


    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor())
                .category(post.getCategory())
                .viewCount(post.getViewCount())
                .thumbnailPath(post.getThumbnailPath())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }



}












